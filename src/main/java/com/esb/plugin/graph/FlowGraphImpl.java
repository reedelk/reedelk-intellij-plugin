package com.esb.plugin.graph;

import com.esb.plugin.graph.node.GraphNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkArgument;

public class FlowGraphImpl implements FlowGraph {

    private final String id;

    private DirectedGraph<GraphNode> graph;

    public FlowGraphImpl(String id) {
        checkArgument(id != null, "id");
        this.id = id;
        this.graph = new DirectedGraph<>();
    }

    private FlowGraphImpl(DirectedGraph<GraphNode> graph, String id) {
        this(id);
        this.graph = graph;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public void add(@NotNull GraphNode n1) {
        graph.addNode(n1);
    }

    @Override
    public void root(@NotNull GraphNode root) {
        graph.root(root);
    }

    @Override
    public void add(@Nullable GraphNode n1, @NotNull GraphNode n2) {
        if (n1 == null) {
            graph.root(n2);
        } else {
            graph.putEdge(n1, n2);
        }
    }

    @Override
    public void add(@NotNull GraphNode n1, @NotNull GraphNode n2, int index) {
        graph.putEdge(n1, n2, index);
    }

    @Override
    public void remove(GraphNode n1) {
        graph.removeNode(n1);
    }

    @Override
    public void remove(GraphNode n1, GraphNode n2) {
        checkArgument(n1 != null, "n1");
        checkArgument(n2 != null, "n2");
        graph.removeEdge(n1, n2);
    }

    @Override
    public List<GraphNode> successors(@NotNull GraphNode n1) {
        return graph.successors(n1);
    }

    @Override
    public List<GraphNode> predecessors(@NotNull GraphNode n1) {
        return graph.predecessors(n1);
    }

    @Override
    public int nodesCount() {
        return graph.nodes().size();
    }

    @Override
    public Collection<GraphNode> nodes() {
        return graph != null ?
                graph.nodes() :
                Collections.emptyList();
    }

    @Override
    public boolean isEmpty() {
        return graph.isEmpty();
    }

    @Override
    public void breadthFirstTraversal(@NotNull Consumer<GraphNode> consumer) {
        if (!graph.isEmpty()) {
            graph.breadthFirstTraversal(graph.root(), consumer);
        }
    }

    @Override
    public GraphNode root() {
        return graph.root();
    }

    @Override
    public FlowGraph copy() {
        return new FlowGraphImpl(graph.copy(), id);
    }

    @Override
    public void removeEdgesStartingFrom(GraphNode node) {
        graph.removeEdgesStartingFrom(node);
    }

}
