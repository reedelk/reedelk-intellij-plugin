package com.reedelk.plugin.editor.properties.componentinput;

import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.JBUI;
import com.reedelk.plugin.editor.properties.commons.DisposableCollapsiblePane;
import com.reedelk.plugin.editor.properties.commons.DisposablePanel;
import com.reedelk.plugin.editor.properties.commons.FormBuilder;
import com.reedelk.plugin.service.module.impl.component.componentio.IOComponent;

import javax.swing.*;

public class ComponentExpectedInput extends AbstractComponentInput {

    private static final int LEFT_OFFSET = 24;

    public ComponentExpectedInput() {
        setBorder(JBUI.Borders.empty(0, 1, 0, 8));
        setBackground(JBColor.WHITE);
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    }

    @Override
    void render(IOComponent IOComponent, DisposablePanel parent) {
        DisposableCollapsiblePane description = new DisposableCollapsiblePane(htmlLabel("<b style=\"color: #666666\">description</b>", "", false), () -> {
            JBLabel label = new JBLabel(htmlLabel("this is a description test", "", false));
            label.setBorder(JBUI.Borders.emptyLeft(LEFT_OFFSET));
            return label;
        });
        description.setBorder(JBUI.Borders.empty());
        FormBuilder.get().addFullWidthAndHeight(description, parent);
    }
}
