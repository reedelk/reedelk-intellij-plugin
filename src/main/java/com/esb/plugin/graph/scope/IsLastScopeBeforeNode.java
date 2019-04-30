package com.esb.plugin.graph.scope;

import com.esb.plugin.graph.FlowGraph;
import com.esb.plugin.graph.node.GraphNode;
import com.esb.plugin.graph.node.ScopedDrawable;

public class IsLastScopeBeforeNode {

    public static boolean of(FlowGraph graph, ScopedDrawable targetScope, GraphNode firstNodeOutsideScope) {
        return FindScope.of(graph, firstNodeOutsideScope)
                .map(currentScope -> currentScope.scopeContains(targetScope))
                .orElseGet(() -> !FindScope.of(graph, targetScope).isPresent());
    }
}
