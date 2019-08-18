package com.reedelk.plugin.component.scanner.property;

import com.reedelk.plugin.component.domain.*;
import com.reedelk.plugin.component.scanner.ComponentAnalyzerContext;
import com.reedelk.plugin.component.scanner.UnsupportedType;
import com.reedelk.runtime.api.annotation.File;
import com.reedelk.runtime.api.annotation.Script;
import io.github.classgraph.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.reedelk.plugin.component.scanner.property.PropertyScannerUtils.filterByFullyQualifiedClassNameType;
import static com.reedelk.plugin.component.scanner.property.PropertyScannerUtils.isEnumeration;
import static com.reedelk.plugin.converter.ValueConverterFactory.isKnownType;
import static java.util.stream.Collectors.toList;

public class PropertyTypeHandler implements Handler {

    @Override
    public void handle(FieldInfo propertyInfo, ComponentPropertyDescriptor.Builder builder, ComponentAnalyzerContext context) {
        TypeSignature typeSignature = propertyInfo.getTypeDescriptor();

        if (typeSignature instanceof BaseTypeSignature) {
            TypeDescriptor typeDescriptor = processPrimitiveType(((BaseTypeSignature) typeSignature).getType(), propertyInfo);
            builder.type(typeDescriptor);

        } else if (typeSignature instanceof ClassRefTypeSignature) {
            ClassRefTypeSignature classRef = (ClassRefTypeSignature) typeSignature;
            TypeDescriptor typeDescriptor = processClassRefType(classRef, propertyInfo, context);
            builder.type(typeDescriptor);

        } else {
            throw new UnsupportedType(typeSignature.getClass());
        }
    }

    private TypeDescriptor processPrimitiveType(Class<?> clazz, FieldInfo fieldInfo) {
        if (isScript(fieldInfo, clazz)) {
            // Find and map auto complete variable annotations.
            Boolean inline = PropertyScannerUtils.getAnnotationParameterValueOrDefault(fieldInfo,
                    Script.class, TypeScriptDescriptor.INLINE_ANNOTATION_PARAM_NAME, true);
            return new TypeScriptDescriptor(inline);
        } else if (isFile(fieldInfo, clazz)) {
            return new TypeFileDescriptor();
        } else {
            return new TypePrimitiveDescriptor(clazz);
        }
    }

    private TypeDescriptor processClassRefType(ClassRefTypeSignature typeSignature, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        String fullyQualifiedClassName = typeSignature.getFullyQualifiedClassName();
        if (isKnownType(fullyQualifiedClassName)) {
            try {
                return processPrimitiveType(Class.forName(fullyQualifiedClassName), fieldInfo);
            } catch (ClassNotFoundException e) {
                // if it is a known type, then the class must be resolvable.
                // Otherwise the @PropertyValueConverterFactory class would not even compile.
                throw new UnsupportedType(fullyQualifiedClassName);
            }
        } else if (isEnumeration(fullyQualifiedClassName, context)) {
            return processEnumType(typeSignature, context);

        } else {
            // We check that we can resolve class info. If we can, then
            ClassInfo classInfo = context.getClassInfo(fullyQualifiedClassName);
            if (classInfo == null) throw new UnsupportedType(typeSignature.getClass());

            Shareable shareable = isShareable(classInfo);
            ComponentPropertyAnalyzer propertyAnalyzer = new ComponentPropertyAnalyzer(context);

            List<ComponentPropertyDescriptor> allProperties = classInfo
                    .getFieldInfo()
                    .stream()
                    .map(propertyAnalyzer::analyze)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(toList());
            return new TypeObjectDescriptor(fullyQualifiedClassName, allProperties, shareable);
        }
    }

    private TypeEnumDescriptor processEnumType(ClassRefTypeSignature enumRefType, ComponentAnalyzerContext context) {
        String enumFullyQualifiedClassName = enumRefType.getFullyQualifiedClassName();
        ClassInfo enumClassInfo = context.getClassInfo(enumFullyQualifiedClassName);
        FieldInfoList declaredFieldInfo = enumClassInfo.getDeclaredFieldInfo();
        List<String> enumNames = declaredFieldInfo
                .stream()
                .filter(filterByFullyQualifiedClassNameType(enumFullyQualifiedClassName))
                .map(FieldInfo::getName)
                .collect(Collectors.toList());
        return new TypeEnumDescriptor(enumNames, enumNames.get(0));
    }

    // A property is a Script if and only if it has @Script annotation AND its type is String
    private boolean isScript(FieldInfo fieldInfo, Class<?> clazz) {
        return fieldInfo.hasAnnotation(Script.class.getName()) &&
                String.class.equals(clazz);
    }

    // A property is a File if and only if it has @File annotation AND its type is String
    private boolean isFile(FieldInfo fieldInfo, Class<?> clazz) {
        return fieldInfo.hasAnnotation(File.class.getName()) &&
                String.class.equals(clazz);
    }

    private Shareable isShareable(ClassInfo classInfo) {
        return classInfo.hasAnnotation(com.reedelk.runtime.api.annotation.Shareable.class.getName()) ?
                Shareable.YES : Shareable.NO;
    }
}
