package com.reedelk.plugin.editor.properties.renderer;

import com.reedelk.plugin.component.domain.TypeDescriptor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static com.reedelk.plugin.component.domain.TypeComboDescriptor.TypeCombo;
import static com.reedelk.plugin.component.domain.TypeFileDescriptor.TypeFile;
import static com.reedelk.plugin.component.domain.TypeObjectDescriptor.TypeObject;
import static com.reedelk.plugin.component.domain.TypeScriptDescriptor.TypeScript;
import static com.reedelk.plugin.component.domain.TypeScriptInlineDescriptor.TypeScriptInline;
import static com.reedelk.plugin.component.type.unknown.UnknownPropertyType.UnknownType;

public class TypeRendererFactory {

    private static final Map<Class<?>, TypePropertyRenderer> RENDERER;

    static {
        Map<Class<?>, TypePropertyRenderer> tmp = new HashMap<>();

        tmp.put(int.class, new IntegerPropertyRenderer());
        tmp.put(Integer.class, new IntegerPropertyRenderer());
        tmp.put(long.class, new LongPropertyRenderer());
        tmp.put(Long.class, new LongPropertyRenderer());
        tmp.put(float.class, new FloatPropertyRenderer());
        tmp.put(Float.class, new FloatPropertyRenderer());
        tmp.put(double.class, new DoublePropertyRenderer());
        tmp.put(Double.class, new DoublePropertyRenderer());
        tmp.put(boolean.class, new BooleanPropertyRenderer());
        tmp.put(Boolean.class, new BooleanPropertyRenderer());

        tmp.put(Enum.class, new EnumPropertyRenderer());
        tmp.put(String.class, new StringPropertyRenderer());
        tmp.put(BigInteger.class, new BigIntegerPropertyRenderer());
        tmp.put(BigDecimal.class, new BigDecimalPropertyRenderer());

        tmp.put(Map.class, new TypeMapPropertyRenderer());

        tmp.put(TypeFile.class, new TypeFilePropertyRenderer());
        tmp.put(TypeCombo.class, new TypeComboPropertyRenderer());
        tmp.put(TypeObject.class, new TypeObjectPropertyRenderer());
        tmp.put(TypeScript.class, new TypeScriptPropertyRenderer());
        tmp.put(TypeScriptInline.class, new TypeScriptInlinePropertyRenderer());

        tmp.put(UnknownType.class, new UnknownPropertyRenderer());

        RENDERER = tmp;
    }

    public static TypeRendererFactory get() {
        return new TypeRendererFactory();
    }

    public TypePropertyRenderer from(TypeDescriptor propertyType) {
        if (RENDERER.containsKey(propertyType.type())) {
            return RENDERER.get(propertyType.type());
        } else {
            throw new IllegalArgumentException("Could not find renderer for type: " + propertyType);
        }
    }
}
