package com.reedelk.plugin.editor.properties.renderer.typeobject.configuration;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.ListCellRendererWrapper;
import com.reedelk.plugin.editor.properties.renderer.commons.InputChangeListener;
import com.reedelk.plugin.service.module.impl.configuration.ConfigMetadata;
import com.reedelk.runtime.api.commons.StringUtils;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Optional;

public class ConfigSelectorCombo extends ComboBox<ConfigMetadata> implements ItemListener {

    private transient InputChangeListener listener;

    public ConfigSelectorCombo() {
        setRenderer(new ConfigMetadataRenderer());
        addItemListener(this);
    }

    @Override
    public void itemStateChanged(ItemEvent event) {
        if (event.getStateChange() == ItemEvent.SELECTED) {
            ConfigMetadata item = (ConfigMetadata) event.getItem();
            if (listener != null) {
                listener.onChange(item);
            }
        }
    }

    public void removeListener() {
        this.listener = null;
    }

    public void addListener(InputChangeListener changeListener) {
        this.listener = changeListener;
    }

    private static class ConfigMetadataRenderer extends ListCellRendererWrapper<ConfigMetadata> {
        @Override
        public void customize(JList list, ConfigMetadata value, int index, boolean selected, boolean hasFocus) {
            if (value == null) return;

            String configTitle = Optional.ofNullable(value.getTitle()).orElse(StringUtils.EMPTY);
            StringBuilder renderedValue = new StringBuilder(configTitle);
            if (renderedValue.length() == 0) {
                renderedValue.append(value.getId());
            }
            if (StringUtils.isNotBlank(value.getFileName())) {
                renderedValue
                        .append(" ")
                        .append("(")
                        .append(value.getFileName())
                        .append(")");
            }
            setText(renderedValue.toString());
        }
    }
}
