package com.esb.plugin.component.scanner;

import com.esb.api.annotation.*;
import com.esb.plugin.component.domain.*;
import com.esb.plugin.converter.ValueConverterFactory;
import io.github.classgraph.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.esb.plugin.component.domain.ComponentPropertyDescriptor.PropertyRequired;
import static com.esb.plugin.component.domain.ComponentPropertyDescriptor.PropertyRequired.NOT_REQUIRED;
import static com.esb.plugin.component.domain.ComponentPropertyDescriptor.PropertyRequired.REQUIRED;
import static com.esb.plugin.converter.ValueConverterFactory.isKnownType;
import static java.util.stream.Collectors.toList;

class ComponentPropertyAnalyzer {

    private static final String ANNOTATION_DEFAULT_PARAM_NAME = "value";

    private final ComponentAnalyzerContext context;

    ComponentPropertyAnalyzer(ComponentAnalyzerContext context) {
        this.context = context;
    }

    Optional<ComponentPropertyDescriptor> analyze(FieldInfo fieldInfo) {
        return fieldInfo.hasAnnotation(Property.class.getName()) ?
                Optional.of(analyzeProperty(fieldInfo)) :
                Optional.empty();
    }

    private ComponentPropertyDescriptor analyzeProperty(FieldInfo propertyInfo) {
        String propertyName = propertyInfo.getName();
        String displayName = getDisplayName(propertyInfo, propertyName);

        TypeDescriptor propertyType = getPropertyType(propertyInfo);
        Object defaultValue = getDefaultValue(propertyInfo, propertyType);

        PropertyRequired required = isRequired(propertyInfo) ? REQUIRED : NOT_REQUIRED;

        return ComponentPropertyDescriptor.builder()
                .displayName(displayName)
                .propertyName(propertyName)
                .type(propertyType)
                .defaultValue(defaultValue)
                .required(required)
                .build();
    }

    private String getDisplayName(FieldInfo propertyInfo, String propertyName) {
        String displayName = getAnnotationValueOrDefault(propertyInfo, Property.class, propertyInfo.getName());
        displayName = Property.USE_DEFAULT_NAME.equals(displayName) ? propertyName : displayName;
        return displayName;
    }

    private TypeDescriptor getPropertyType(FieldInfo fieldInfo) {
        TypeSignature typeSignature = fieldInfo.getTypeDescriptor();
        if (typeSignature instanceof BaseTypeSignature) {
            return processPrimitiveType(((BaseTypeSignature) typeSignature).getType(), fieldInfo);
        } else if (typeSignature instanceof ClassRefTypeSignature) {
            ClassRefTypeSignature classRef = (ClassRefTypeSignature) typeSignature;
            return processClassRefType(classRef, fieldInfo);
        } else {
            throw new UnsupportedType(typeSignature.getClass());
        }
    }

    private TypeDescriptor processPrimitiveType(Class<?> clazz, FieldInfo fieldInfo) {
        if (isScript(fieldInfo, clazz)) {
            // Find and map auto complete variable annotations.
            return new TypeScriptDescriptor();
        } else if (isFile(fieldInfo, clazz)) {
            return new TypeFileDescriptor();
        } else {
            return new TypePrimitiveDescriptor(clazz);
        }
    }

    private TypeDescriptor processClassRefType(ClassRefTypeSignature typeSignature, FieldInfo fieldInfo) {
        String fullyQualifiedClassName = typeSignature.getFullyQualifiedClassName();
        if (isKnownType(fullyQualifiedClassName)) {
            try {
                return processPrimitiveType(Class.forName(fullyQualifiedClassName), fieldInfo);
            } catch (ClassNotFoundException e) {
                // if it is a known type, then the class must be resolvable.
                // Otherwise the @PropertyValueConverterFactory class would not even compile.
                throw new UnsupportedType(fullyQualifiedClassName);
            }
        } else if (isEnumeration(fullyQualifiedClassName)) {
            return processEnumType(typeSignature);

        } else {
            // We check that we can resolve class info. If we can, then
            ClassInfo classInfo = context.getClassInfo(fullyQualifiedClassName);
            if (classInfo == null) throw new UnsupportedType(typeSignature.getClass());

            boolean shareable = isShareable(classInfo);
            List<ComponentPropertyDescriptor> collect = classInfo
                    .getFieldInfo()
                    .stream()
                    .map(this::analyze)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(toList());
            return new TypeObjectDescriptor(fullyQualifiedClassName, shareable, collect);
        }
    }

    // TODO: Test corner cases like when there is an enum with no fields!
    private TypeEnumDescriptor processEnumType(ClassRefTypeSignature enumRefType) {
        String enumFullyQualifiedClassName = enumRefType.getFullyQualifiedClassName();
        ClassInfo enumClassInfo = context.getClassInfo(enumFullyQualifiedClassName);
        FieldInfoList declaredFieldInfo = enumClassInfo.getDeclaredFieldInfo();
        List<String> enumNames = declaredFieldInfo
                .stream()
                .filter(FilterByFullyQualifiedClassNameType(enumFullyQualifiedClassName))
                .map(FieldInfo::getName)
                .collect(Collectors.toList());
        return new TypeEnumDescriptor(enumNames, enumNames.get(0));
    }

    private boolean isEnumeration(String fullyQualifiedClassName) {
        ClassInfo classInfo = context.getClassInfo(fullyQualifiedClassName);
        return classInfo
                .getSuperclasses()
                .stream()
                .anyMatch(info -> info.getName().equals(Enum.class.getName()));
    }

    private Object getDefaultValue(FieldInfo propertyInfo, TypeDescriptor propertyType) {
        String stringValue = getAnnotationValueOrDefault(propertyInfo, Default.class, Default.USE_DEFAULT_VALUE);
        return Default.USE_DEFAULT_VALUE.equals(stringValue) ?
                propertyType.defaultValue() :
                ValueConverterFactory.forType(propertyType).from(stringValue);
    }

    @SuppressWarnings("unchecked")
    private <T> T getAnnotationValueOrDefault(FieldInfo fieldInfo, Class<?> annotationClazz, T defaultValue) {
        if (!fieldInfo.hasAnnotation(annotationClazz.getName())) {
            return defaultValue;
        }
        AnnotationInfo annotationInfo = fieldInfo.getAnnotationInfo(annotationClazz.getName());
        AnnotationParameterValueList parameterValues = annotationInfo.getParameterValues();
        return parameterValues.get(ANNOTATION_DEFAULT_PARAM_NAME) == null ?
                defaultValue :
                (T) parameterValues.getValue(ANNOTATION_DEFAULT_PARAM_NAME);
    }

    /**
     * Returns a new Predicate which filters FieldInfo's having type the target
     * class name specified in the argument of this function.
     */
    private static Predicate<FieldInfo> FilterByFullyQualifiedClassNameType(String targetFullyQualifiedClassName) {
        return fieldInfo -> {
            TypeSignature typeDescriptor = fieldInfo.getTypeDescriptor();
            if (typeDescriptor instanceof ClassRefTypeSignature) {
                ClassRefTypeSignature matchingClass = (ClassRefTypeSignature) typeDescriptor;
                return matchingClass.getFullyQualifiedClassName()
                        .equals(targetFullyQualifiedClassName);
            }
            return false;
        };
    }

    // A property is a Script if and only if it has
    // @Script annotation AND type String
    private boolean isScript(FieldInfo fieldInfo, Class<?> clazz) {
        return fieldInfo.hasAnnotation(Script.class.getName()) &&
                String.class.equals(clazz);
    }

    // A property is a File if and only if it has
    // @File annotation AND type String
    private boolean isFile(FieldInfo fieldInfo, Class<?> clazz) {
        return fieldInfo.hasAnnotation(File.class.getName()) &&
                String.class.equals(clazz);
    }

    private boolean isShareable(ClassInfo classInfo) {
        return classInfo.hasAnnotation(Shareable.class.getName());
    }

    private boolean isRequired(FieldInfo fieldInfo) {
        return fieldInfo.hasAnnotation(Required.class.getName());
    }
}
