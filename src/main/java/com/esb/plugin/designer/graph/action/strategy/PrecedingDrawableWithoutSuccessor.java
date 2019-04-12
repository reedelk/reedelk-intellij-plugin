package com.esb.plugin.designer.graph.action.strategy;

import com.esb.plugin.designer.graph.FlowGraph;
import com.esb.plugin.designer.graph.ScopeUtilities;
import com.esb.plugin.designer.graph.connector.Connector;
import com.esb.plugin.designer.graph.drawable.Drawable;
import com.esb.plugin.designer.graph.drawable.ScopedDrawable;

import java.awt.*;
import java.util.Stack;

/**
 * Handles the case where the closest preceding node is the LAST NODE.
 * If the closest preceding node belongs to a scope, then:
 * - if it is within the scope x edge, then add it to that scope.
 * - if it is NOT within the scope x edge, then finds in which scope it
 * belongs to and connect it from the last elements of the last innermost scope.
 * <p>
 * If the closest preceding node does not belong to a scope, then:
 * - Add the node as successor.
 */
public class PrecedingDrawableWithoutSuccessor extends AbstractAddStrategy {

    public PrecedingDrawableWithoutSuccessor(FlowGraph graph, Point dropPoint, Connector connector) {
        super(graph, dropPoint, connector);
    }

    @Override
    public void execute(Drawable closestPrecedingDrawable) {

        Stack<ScopedDrawable> scopes = ScopeUtilities.findScopesOf(graph, closestPrecedingDrawable);

        if (scopes.isEmpty()) {
            connector.addPredecessor(closestPrecedingDrawable);
            return;
        }

        ScopedDrawable currentScope = null;
        ScopedDrawable lastInnerMostScope = null;

        while (!scopes.isEmpty()) {

            currentScope = scopes.pop();

            int maxXBound = ScopeUtilities.getScopeMaxXBound(graph, currentScope);

            if (dropPoint.x <= maxXBound) break;

            lastInnerMostScope = currentScope;

            if (scopes.isEmpty()) {
                currentScope = null;
            }
        }

        if (lastInnerMostScope != null) {
            ScopeUtilities
                    .listLastDrawablesOfScope(graph, lastInnerMostScope)
                    .forEach(connector::addPredecessor);

        } else {
            connector.addPredecessor(closestPrecedingDrawable);
        }

        if (currentScope != null) {
            connector.addToScope(currentScope);
        }
    }
}
