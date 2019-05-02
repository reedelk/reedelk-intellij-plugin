package com.esb.plugin.component;

import com.esb.plugin.designer.canvas.drawables.ComponentDescription;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Component {

    private final ComponentDescriptor descriptor;

    private Map<String, Object> componentData = new HashMap<>();

    public Component(final ComponentDescriptor descriptor) {
        this.descriptor = descriptor;
        this.componentData.put(ComponentDescription.DESCRIPTION_PROPERTY_NAME, descriptor.getDisplayName());
    }

    public Object getData(String key) {
        return componentData.get(key);
    }

    public void setPropertyValue(String propertyName, Object propertyValue) {
        this.componentData.put(propertyName, propertyValue);
    }

    public String getFullyQualifiedName() {
        return descriptor.getFullyQualifiedName();
    }

    public String getDisplayName() {
        return descriptor.getDisplayName();
    }

    public List<String> componentDataKeys() {
        return descriptor.componentDataKeys();
    }

    public List<String> getPropertiesNames() {
        return descriptor.getPropertiesNames();
    }
}
