package com.esb.plugin.assertion.component;

import com.esb.plugin.component.domain.ComponentPropertyDescriptor;
import com.esb.plugin.component.domain.TypeDescriptor;

import static com.esb.plugin.component.domain.ComponentPropertyDescriptor.PropertyRequired.NOT_REQUIRED;
import static com.esb.plugin.component.domain.ComponentPropertyDescriptor.PropertyRequired.REQUIRED;
import static org.assertj.core.api.Assertions.assertThat;

public class ComponentPropertyDescriptorAssertion {

    private final ComponentPropertyDescriptor propertyDescriptor;

    public ComponentPropertyDescriptorAssertion(ComponentPropertyDescriptor propertyDescriptor) {
        this.propertyDescriptor = propertyDescriptor;
    }

    public ComponentPropertyDescriptorAssertion hasName(String expectedName) {
        assertThat(propertyDescriptor.getPropertyName()).isEqualTo(expectedName);
        return this;
    }

    public ComponentPropertyDescriptorAssertion hasDisplayName(String expectedDisplayName) {
        assertThat(propertyDescriptor.getDisplayName()).isEqualTo(expectedDisplayName);
        return this;
    }

    public ComponentPropertyDescriptorAssertion required() {
        assertThat(propertyDescriptor.required()).isEqualTo(REQUIRED);
        return this;
    }

    public ComponentPropertyDescriptorAssertion notRequired() {
        assertThat(propertyDescriptor.required()).isEqualTo(NOT_REQUIRED);
        return this;
    }

    public ComponentPropertyDescriptorAssertion hasDefaultValue(Object object) {
        assertThat(propertyDescriptor.getDefaultValue()).isEqualTo(object);
        return this;
    }

    public ComponentPropertyDescriptorAssertion hasType(TypeDescriptor descriptor) {
        assertThat(propertyDescriptor.getPropertyType()).isEqualTo(descriptor);
        return this;
    }

}
