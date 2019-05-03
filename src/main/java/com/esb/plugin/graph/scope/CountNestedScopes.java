package com.esb.plugin.graph.scope;

import com.esb.plugin.graph.FlowGraph;
import com.esb.plugin.graph.node.GraphNode;
import com.esb.plugin.graph.node.ScopedGraphNode;

public class CountNestedScopes {

    public static int of(FlowGraph graph, GraphNode target) {
        if (target instanceof ScopedGraphNode) {
            ScopedGraphNode scopedGraphNode = (ScopedGraphNode) target;
            if (scopedGraphNode.getScope().isEmpty()) {
                return 1 + FindScope.of(graph, scopedGraphNode)
                        .map(scope -> 1 + of(graph, scope))
                        .orElse(0);
            }
        }
        return FindScope.of(graph, target)
                .map(scopedDrawable -> 1 + of(graph, scopedDrawable))
                .orElse(0);
    }
}
