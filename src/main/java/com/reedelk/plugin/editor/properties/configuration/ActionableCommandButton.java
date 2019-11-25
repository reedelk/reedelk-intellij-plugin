package com.reedelk.plugin.editor.properties.configuration;

import com.reedelk.plugin.editor.properties.widget.ClickableLabel;
import com.reedelk.plugin.editor.properties.widget.input.ConfigSelector;
import com.reedelk.plugin.service.module.impl.ConfigMetadata;

import javax.swing.*;
import java.awt.event.MouseEvent;

public abstract class ActionableCommandButton extends ClickableLabel implements ConfigSelector.SelectListener {

    private ConfigMetadata selectedMetadata;

    ActionableCommandButton(String text, Icon icon, Icon disabledIcon) {
        super(text, icon, disabledIcon);
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        onClick(selectedMetadata);
    }

    @Override
    public void onSelect(ConfigMetadata configMetadata) {
        this.selectedMetadata = configMetadata;
    }

    protected abstract void onClick(ConfigMetadata selectedMetadata);

}
