package com.reedelk.plugin.editor.properties.renderer.typescript;

import com.intellij.openapi.module.Module;
import com.reedelk.plugin.component.domain.ComponentPropertyDescriptor;
import com.reedelk.plugin.editor.properties.accessor.PropertyAccessor;
import com.reedelk.plugin.editor.properties.commons.ContainerContext;
import com.reedelk.plugin.editor.properties.commons.DisposablePanel;
import com.reedelk.plugin.editor.properties.renderer.AbstractPropertyTypeRenderer;
import com.reedelk.plugin.editor.properties.renderer.typescript.scriptactions.ScriptActionsPanel;
import com.reedelk.plugin.service.module.ScriptService;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.EAST;

public class ScriptPropertyRenderer extends AbstractPropertyTypeRenderer {

    @NotNull
    @Override
    public JComponent render(@NotNull Module module,
                             @NotNull ComponentPropertyDescriptor propertyDescriptor,
                             @NotNull PropertyAccessor propertyAccessor,
                             @NotNull ContainerContext context) {

        List<String> scripts = ScriptService.getInstance(module).listAllScripts();

        ScriptActionsPanel scriptActionsPanel = new ScriptActionsPanel(module);

        ScriptSelector scriptSelector = new ScriptSelector(scripts);
        scriptSelector.setSelectedItem(propertyAccessor.get());
        scriptActionsPanel.onSelect(propertyAccessor.get());
        scriptSelector.addListener(value -> {
            propertyAccessor.set(value);
            scriptActionsPanel.onSelect((String) value);
        });

        JPanel container = new DisposablePanel();
        container.setLayout(new BorderLayout());
        container.add(scriptSelector, CENTER);
        container.add(scriptActionsPanel, EAST);
        return container;
    }
}
