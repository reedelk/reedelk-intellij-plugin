package com.esb.plugin.component.domain;

public class TypeFileDescriptor implements TypeDescriptor {

    private final String defaultFile = "";

    @Override
    public Class<?> type() {
        return TypeFile.class;
    }

    @Override
    public Object defaultValue() {
        return defaultFile;
    }

    public static class TypeFile {
    }
}
