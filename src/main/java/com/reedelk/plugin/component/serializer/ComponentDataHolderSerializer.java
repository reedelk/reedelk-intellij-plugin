package com.reedelk.plugin.component.serializer;

import com.reedelk.module.descriptor.model.ComponentDataHolder;
import com.reedelk.module.descriptor.model.PropertyDescriptor;
import com.reedelk.module.descriptor.model.TypeObjectDescriptor;
import com.reedelk.plugin.component.ComponentData;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.List;

public class ComponentDataHolderSerializer {

    private static final ComponentDataHolderSerializer INSTANCE = new ComponentDataHolderSerializer();

    private ComponentDataHolderSerializer() {
    }

    public static ComponentDataHolderSerializer get() {
        return INSTANCE;
    }

    public void serialize(@NotNull ComponentData componentData,
                          @NotNull JSONObject parent) {
        List<PropertyDescriptor> propertiesDescriptors = componentData.getPropertiesDescriptors();
        TypeObjectSerializer.get().serialize(componentData, parent, propertiesDescriptors);
    }

    public void serialize(@NotNull TypeObjectDescriptor typeObjectDescriptor,
                          @NotNull JSONObject jsonObject,
                          @NotNull ComponentDataHolder dataHolder) {
        List<PropertyDescriptor> propertiesDescriptor = typeObjectDescriptor.getObjectProperties();
        TypeObjectSerializer.get().serialize(dataHolder, jsonObject, propertiesDescriptor);
    }
}
