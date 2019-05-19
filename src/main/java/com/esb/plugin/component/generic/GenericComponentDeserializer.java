package com.esb.plugin.component.generic;

import com.esb.internal.commons.JsonParser;
import com.esb.plugin.component.ComponentData;
import com.esb.plugin.component.TypeDescriptor;
import com.esb.plugin.converter.ValueConverterFactory;
import com.esb.plugin.graph.FlowGraph;
import com.esb.plugin.graph.deserializer.AbstractDeserializer;
import com.esb.plugin.graph.deserializer.DeserializerContext;
import com.esb.plugin.graph.node.GraphNode;
import org.json.JSONObject;

public class GenericComponentDeserializer extends AbstractDeserializer {

    public GenericComponentDeserializer(FlowGraph graph, DeserializerContext context) {
        super(graph, context);
    }

    @Override
    public GraphNode deserialize(GraphNode parent, JSONObject jsonDefinition) {

        String name = JsonParser.Implementor.name(jsonDefinition);

        GenericComponentNode node = context.instantiateGraphNode(name);

        ComponentData componentData = node.componentData();

        // fill up data from componentData definition
        componentData.descriptorProperties().forEach(propertyName -> {
            propertyName = propertyName.toLowerCase();

            // TODO: we should say if mandatory or not with annotations
            if (jsonDefinition.has(propertyName)) {

                TypeDescriptor propertyType = componentData.getPropertyType(propertyName);

                Object propertyValue = ValueConverterFactory.forType(propertyType)
                        .from(propertyName, jsonDefinition);

                // TODO: Should this be in the converter!?!?
                // Explicitly map JSON Library NULL value to java's null.
                if (propertyValue == JSONObject.NULL) {
                    propertyValue = null;
                }

                componentData.set(propertyName, propertyValue);
            }
        });

        graph.add(parent, node);
        return node;
    }

}
