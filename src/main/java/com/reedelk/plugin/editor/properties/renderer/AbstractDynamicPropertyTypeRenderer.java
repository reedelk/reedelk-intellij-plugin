package com.reedelk.plugin.editor.properties.renderer;

import com.intellij.openapi.module.Module;
import com.reedelk.module.descriptor.model.property.PropertyDescriptor;
import com.reedelk.plugin.editor.properties.commons.DynamicValueField;
import com.reedelk.plugin.editor.properties.commons.DynamicValueInputFieldAdapter;
import com.reedelk.plugin.editor.properties.context.ContainerContext;
import com.reedelk.plugin.editor.properties.context.PropertyAccessor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public abstract class AbstractDynamicPropertyTypeRenderer extends AbstractPropertyTypeRenderer {

    @NotNull
    @Override
    public JComponent render(@NotNull Module module,
                             @NotNull PropertyDescriptor propertyDescriptor,
                             @NotNull PropertyAccessor propertyAccessor,
                             @NotNull ContainerContext context) {
        String hint = propertyDescriptor.getHintValue();
        String componentPropertyPath = context.getPropertyPath(propertyDescriptor.getName());

        Object initValue = propertyAccessor.get();

        DynamicValueInputFieldAdapter inputFieldAdapter = inputFieldAdapter(hint);
        DynamicValueField field =
                new DynamicValueField(module, inputFieldAdapter, componentPropertyPath, context, initValue);
        field.addListener(propertyAccessor::set);
        return field;
    }

    protected abstract DynamicValueInputFieldAdapter inputFieldAdapter(String hint);

}
