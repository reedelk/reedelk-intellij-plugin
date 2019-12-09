package com.reedelk.plugin.editor.properties.renderer.typemap;

import com.intellij.openapi.module.Module;
import com.reedelk.plugin.commons.VectorUtils;
import com.reedelk.plugin.editor.properties.accessor.PropertyAccessor;
import com.reedelk.plugin.editor.properties.commons.ContainerContext;

import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

public class MapPropertyRenderer extends BaseMapPropertyRenderer {

    @SuppressWarnings("unchecked")
    @Override
    protected JComponent getContent(Module module, PropertyAccessor propertyAccessor, ContainerContext context) {
        MapTableModel tableModel = new MapTableModel(vectors -> {
            // Data Model Update
            Map<String, String> updated = new LinkedHashMap<>();
            vectors.forEach(vector -> {
                String key = VectorUtils.getOrEmptyIfNull((Vector<String>) vector, 0); // 0 is the key
                String value = VectorUtils.getOrEmptyIfNull((Vector<String>) vector, 1); // 1 is the value
                updated.put(key, value);
            });
            propertyAccessor.set(updated);
        });

        // Data Model Initialize
        Map<String, String> map = propertyAccessor.get();
        if (map != null) {
            map.forEach((key, value) -> tableModel.addRow(new Object[]{key, value}));
        }

        // Return the content
        return new MapPropertyTabContainer(module, tableModel);
    }
}
