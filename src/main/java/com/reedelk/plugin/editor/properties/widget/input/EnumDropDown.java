package com.reedelk.plugin.editor.properties.widget.input;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.ListCellRendererWrapper;
import org.apache.commons.collections.KeyValue;
import org.apache.commons.collections.keyvalue.DefaultMapEntry;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class EnumDropDown extends ComboBox<KeyValue> implements ItemListener {

    private static final int WIDTH = 150;

    private InputChangeListener<String> listener;

    private final List<KeyValue> keyValues;

    public EnumDropDown(Map<String, String> valueAndDisplayNameMap) {
        this.keyValues = valueAndDisplayNameMap
                .entrySet()
                .stream()
                .map(entry -> new DefaultMapEntry(entry.getKey(), entry.getValue()))
                .collect(toList());

        DefaultComboBoxModel<KeyValue> hello = new DefaultComboBoxModel<>();
        keyValues.forEach(hello::addElement);
        setModel(hello);
        setRenderer(new ItemRenderer());
        getPreferredSize().width = WIDTH;
        addItemListener(this);
    }

    @Override
    public void itemStateChanged(ItemEvent event) {
        if (event.getStateChange() == ItemEvent.SELECTED) {
            KeyValue item = (KeyValue) event.getItem();
            if (listener != null) {
                listener.onChange((String) item.getKey());
            }
        }
    }

    public void addListener(InputChangeListener<String> changeListener) {
        this.listener = changeListener;
    }

    public void setValue(Object value) {
        Optional<KeyValue> matching = keyValues.stream()
                .filter(keyValue -> keyValue.getKey().equals(value)).findFirst();
        KeyValue keyValue = matching.orElseThrow(() ->
                new IllegalStateException(format("Could not find matching enum value for [%s]", value)));
        setSelectedItem(keyValue);
    }

    class ItemRenderer extends ListCellRendererWrapper<KeyValue> {
        @Override
        public void customize(JList list, KeyValue value, int index, boolean selected, boolean hasFocus) {
            if (value != null) {
                setText((String) value.getValue());
            }
        }
    }
}
