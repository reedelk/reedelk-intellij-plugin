package com.reedelk.plugin.editor.properties.renderer;

import com.intellij.openapi.module.Module;
import com.reedelk.plugin.component.domain.ComponentPropertyDescriptor;
import com.reedelk.plugin.component.domain.VariableDefinition;
import com.reedelk.plugin.editor.properties.accessor.PropertyAccessor;
import com.reedelk.plugin.editor.properties.widget.ContainerContext;
import com.reedelk.plugin.editor.properties.widget.input.script.ScriptContextManager;
import com.reedelk.plugin.editor.properties.widget.input.script.ScriptInputInlineField;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;

public class TypeScriptInlinePropertyRenderer extends AbstractTypePropertyRenderer {

    @NotNull
    @Override
    public JComponent render(@NotNull Module module,
                             @NotNull ComponentPropertyDescriptor propertyDescriptor,
                             @NotNull PropertyAccessor propertyAccessor,
                             @NotNull ContainerContext context) {

        List<VariableDefinition> variableDefinitions = propertyDescriptor.getVariableDefinitions();
        ScriptContextManager scriptContext = new ScriptContextManager(module, context, variableDefinitions);

        ScriptInputInlineField field =
                new ScriptInputInlineField(module, scriptContext, propertyDescriptor.getHintValue());
        field.setValue(propertyAccessor.get());
        field.addListener(propertyAccessor::set);
        return field;
    }
}
