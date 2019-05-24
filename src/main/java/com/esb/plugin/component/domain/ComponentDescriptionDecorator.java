package com.esb.plugin.component.domain;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.esb.internal.commons.JsonParser.Implementor;

/**
 * Decorator which adds the default "Description" property to all registered components.
 */
// TODO: Maybe Component Descriptor should be declared as interface instead.
public class ComponentDescriptionDecorator implements ComponentDescriptor {

    private ComponentDescriptor wrapped;
    private final ComponentPropertyDescriptor descriptionDescriptor;

    public ComponentDescriptionDecorator(ComponentDescriptor descriptor) {
        this.wrapped = descriptor;

        PrimitiveTypeDescriptor typeDescriptor = new PrimitiveTypeDescriptor(String.class);
        descriptionDescriptor = new ComponentPropertyDescriptor(
                Implementor.description(), typeDescriptor, "Description", wrapped.getDisplayName(), false);
    }

    @Override
    public String getFullyQualifiedName() {
        return wrapped.getFullyQualifiedName();
    }

    @Override
    public String getDisplayName() {
        return wrapped.getDisplayName();
    }

    @Override
    public Optional<ComponentPropertyDescriptor> getPropertyDescriptor(String propertyName) {
        if (propertyName.equals(Implementor.description())) {
            return Optional.of(descriptionDescriptor);
        } else {
            return wrapped.getPropertyDescriptor(propertyName);
        }
    }

    @Override
    public List<ComponentPropertyDescriptor> getPropertiesDescriptors() {
        List<ComponentPropertyDescriptor> descriptionDescriptorList = new ArrayList<>();
        descriptionDescriptorList.add(descriptionDescriptor);

        List<ComponentPropertyDescriptor> propertiesDescriptors = wrapped.getPropertiesDescriptors();
        descriptionDescriptorList.addAll(propertiesDescriptors);

        return Collections.unmodifiableList(descriptionDescriptorList);
    }

    @Override
    public boolean isHidden() {
        return wrapped.isHidden();
    }

    @Override
    public Icon getIcon() {
        return wrapped.getIcon();
    }

    @Override
    public Image getImage() {
        return wrapped.getImage();
    }

    @Override
    public boolean equals(Object o) {
        return wrapped.equals(o);
    }

    @Override
    public int hashCode() {
        return wrapped.hashCode();
    }

}
