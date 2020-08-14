package com.reedelk.plugin.action.openapi.serializer;

import com.reedelk.openapi.commons.NavigationPath;
import com.reedelk.openapi.v3.SerializerContext;
import com.reedelk.openapi.v3.model.ParameterObject;
import com.reedelk.plugin.action.openapi.OpenApiImporterContext;
import com.reedelk.plugin.action.openapi.OpenApiUtils;
import com.reedelk.plugin.template.AssetProperties;

import java.util.Map;

class ParameterObjectSerializer extends com.reedelk.openapi.v3.serializer.ParameterObjectSerializer {

    private final OpenApiImporterContext context;

    public ParameterObjectSerializer(OpenApiImporterContext context) {
        this.context = context;
    }

    @Override
    public Map<String, Object> serialize(SerializerContext serializerContext, NavigationPath navigationPath, ParameterObject input) {
        Map<String, Object> serialize = super.serialize(serializerContext, navigationPath, input);
        if (input.getSchema() != null) {
            String data = context.getSchemaFormat().dump(input.getSchema());
            AssetProperties properties = new AssetProperties(data);

            String finalFileName = OpenApiUtils.parameterSchemaFileNameFrom(navigationPath, context);
            String schemaAssetPath = context.createAsset(finalFileName, properties);
            serialize.put(ParameterObject.Properties.SCHEMA.value(), schemaAssetPath);
            // TODO: serialize.put("predefinedSchema", "NONE");
        }
        return serialize;
    }
}
