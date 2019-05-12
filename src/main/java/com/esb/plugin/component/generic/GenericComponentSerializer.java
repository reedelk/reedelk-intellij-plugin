package com.esb.plugin.component.generic;

import com.esb.plugin.component.ComponentData;
import com.esb.plugin.graph.FlowGraph;
import com.esb.plugin.graph.node.GraphNode;
import com.esb.plugin.graph.serializer.AbstractNodeSerializer;
import com.esb.plugin.graph.serializer.JsonObjectFactory;
import org.json.JSONObject;

import static com.esb.internal.commons.JsonParser.Implementor;

public class GenericComponentSerializer extends AbstractNodeSerializer {

    @Override
    protected JSONObject serializeNode(FlowGraph graph, GraphNode node, GraphNode stop) {
        ComponentData componentData = node.componentData();

        JSONObject componentAsJson = JsonObjectFactory.newJSONObject();

        Implementor.name(componentData.getFullyQualifiedName(), componentAsJson);

        componentData.descriptorProperties().forEach(propertyName -> {

            Object data = componentData.getOrDefault(propertyName.toLowerCase(), JSONObject.NULL);

            componentAsJson.put(propertyName.toLowerCase(), data);

        });

        return componentAsJson;
    }
}
