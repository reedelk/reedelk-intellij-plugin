package com.esb.plugin.component.scanner.property;

import com.esb.api.annotation.Variable;
import com.esb.api.annotation.Variables;
import com.esb.plugin.component.domain.ComponentPropertyDescriptor;
import com.esb.plugin.component.domain.VariableDefinition;
import com.esb.plugin.component.scanner.ComponentAnalyzerContext;
import com.intellij.openapi.diagnostic.Logger;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.AnnotationParameterValue;
import io.github.classgraph.AnnotationParameterValueList;
import io.github.classgraph.FieldInfo;

public class PropertyVariableHandler implements Handler {

    private static final Logger LOG = Logger.getInstance(PropertyVariableHandler.class);

    @Override
    public void handle(FieldInfo propertyInfo, ComponentPropertyDescriptor.Builder builder, ComponentAnalyzerContext context) {
        // Check if there are multiple annotations
        boolean hasVariableAnnotation = propertyInfo.hasAnnotation(Variable.class.getName());
        if (hasVariableAnnotation) {
            AnnotationInfo info = propertyInfo.getAnnotationInfo(Variable.class.getName());
            VariableDefinition variableDefinition = processAutocompleteVariableInfo(info);
            builder.autocompleteVariable(variableDefinition);
        }

        // More than one variable definition
        boolean hasVariablesAnnotation = propertyInfo.hasAnnotation(Variables.class.getName());
        if (hasVariablesAnnotation) {
            AnnotationInfo annotationInfo = propertyInfo.getAnnotationInfo(Variables.class.getName());
            AnnotationParameterValueList autocompleteVariablesList = annotationInfo.getParameterValues();
            Object[] annotationInfos = (Object[]) autocompleteVariablesList.get(0).getValue();

            for (Object info : annotationInfos) {
                try {
                    VariableDefinition variableDefinition = processAutocompleteVariableInfo((AnnotationInfo) info);
                    builder.autocompleteVariable(variableDefinition);
                } catch (Exception e) {
                    LOG.warn(String.format("Could not process AutocompleteVariables info for property named '%s'", propertyInfo.getName()), e);
                }
            }
        }
    }

    private VariableDefinition processAutocompleteVariableInfo(AnnotationInfo info) {
        String variableName = getParameterValue(info, "variableName");
        String contextName = getParameterValue(info, "contextName");
        return new VariableDefinition(variableName, contextName);
    }

    private String getParameterValue(AnnotationInfo info, String parameterName) {
        AnnotationParameterValueList parameterValues = info.getParameterValues();
        AnnotationParameterValue parameterValue = parameterValues.get(parameterName);
        return parameterValue != null ? (String) parameterValue.getValue() : null;
    }
}
