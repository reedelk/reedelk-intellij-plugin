package com.reedelk.plugin.converter;

public class DynamicByteArrayConverter extends AbstractDynamicConverter<String> {

    private StringConverter delegate = new StringConverter();

    @Override
    protected ValueConverter<String> delegate() {
        return delegate;
    }
}