package com.reedelk.plugin.graph.deserializer;

import com.reedelk.plugin.graph.FlowGraph;
import com.reedelk.plugin.graph.node.GraphNode;

public abstract class AbstractNodeDeserializer implements Deserializer {

    protected final FlowGraph graph;
    protected final GraphNode current;
    protected final DeserializerContext context;

    public AbstractNodeDeserializer(FlowGraph graph, GraphNode current, DeserializerContext context) {
        this.graph = graph;
        this.context = context;
        this.current = current;
    }
}
