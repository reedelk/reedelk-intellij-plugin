package com.esb.plugin.graph.connector;

import com.esb.plugin.graph.node.GraphNode;
import com.esb.plugin.graph.node.ScopedDrawable;

public interface Connector {

    void add();

    void root();

    void addToScope(ScopedDrawable scope);

    void addSuccessor(GraphNode successor);

    void addPredecessor(GraphNode predecessor);

    void addPredecessor(ScopedDrawable predecessor, int index);

}
