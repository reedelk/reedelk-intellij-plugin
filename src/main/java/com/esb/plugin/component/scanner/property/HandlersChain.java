package com.esb.plugin.component.scanner.property;

import com.esb.plugin.component.domain.ComponentPropertyDescriptor;
import com.esb.plugin.component.scanner.ComponentAnalyzerContext;
import io.github.classgraph.FieldInfo;

import java.util.Arrays;
import java.util.List;

public class HandlersChain {

    private static final List<Handler> HANDLERS = Arrays.asList(
            new PropertyNameHandler(),
            new PropertyTypeHandler(),
            new PropertyRequiredHandler(),
            new PropertyVariableHandler(),
            new PropertyDisplayNameHandler(),
            new PropertyDefaultValueHandler(),
            new PropertyAutocompleteContextHandler());

    public static ComponentPropertyDescriptor descriptor(FieldInfo propertyInfo, ComponentAnalyzerContext context) {
        ComponentPropertyDescriptor.Builder builder = ComponentPropertyDescriptor.builder();
        HANDLERS.stream().forEachOrdered(handler -> handler.handle(propertyInfo, builder, context));
        return builder.build();
    }
}
