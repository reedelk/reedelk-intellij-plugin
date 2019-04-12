package com.esb.plugin.designer.graph.action.strategy;

import com.esb.plugin.designer.Tile;
import com.esb.plugin.designer.graph.FlowGraph;
import com.esb.plugin.designer.graph.ScopeUtilities;
import com.esb.plugin.designer.graph.connector.Connector;
import com.esb.plugin.designer.graph.drawable.Drawable;
import com.esb.plugin.designer.graph.drawable.ScopedDrawable;

import java.awt.*;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkState;

public class PrecedingDrawableWithOneSuccessor extends AbstractAddStrategy {


    public PrecedingDrawableWithOneSuccessor(FlowGraph graph, Point dropPoint, Connector connector) {
        super(graph, dropPoint, connector);
    }

    @Override
    public void execute(Drawable closestPrecedingDrawable) {
        List<Drawable> successors = graph.successors(closestPrecedingDrawable);
        checkState(successors.size() == 1, "Successors size MUST be 1, otherwise it is a Scoped Drawable");

        Drawable successorOfClosestPrecedingNode = successors.get(0);

        if (ScopeUtilities.haveSameScope(graph, closestPrecedingDrawable, successorOfClosestPrecedingNode)) {
            if (withinYBounds(dropPoint.y, closestPrecedingDrawable)) {
                connector.addPredecessor(closestPrecedingDrawable);
                connector.addSuccessor(successorOfClosestPrecedingNode);
                graph.remove(closestPrecedingDrawable, successorOfClosestPrecedingNode);
                addToScopeIfNeeded(closestPrecedingDrawable);
            }
            return;
        }

        // They belong to different scopes

        Optional<ScopedDrawable> optionalPrecedingNode = ScopeUtilities.findScope(graph, closestPrecedingDrawable);

        if (optionalPrecedingNode.isPresent()) {
            // The drop point is inside the closestPrecedingNodeScope
            ScopedDrawable closestPrecedingNodeScope = optionalPrecedingNode.get();
            if (dropPoint.x <= ScopeUtilities.getScopeMaxXBound(graph, closestPrecedingNodeScope)) {
                connector.addPredecessor(closestPrecedingDrawable);
                connector.addToScope(closestPrecedingNodeScope);
                connector.addSuccessor(successorOfClosestPrecedingNode);
                graph.remove(closestPrecedingDrawable, successorOfClosestPrecedingNode);

            } else {
                // The drop point is outside the closestPrecedingNodeScope
                // Find the scope where it belongs to.
                ScopeUtilities.listLastDrawablesOfScope(graph, closestPrecedingNodeScope)
                        .forEach(lastNode -> {
                            connector.addPredecessor(lastNode);
                            graph.remove(lastNode, successorOfClosestPrecedingNode);
                        });
                connector.addSuccessor(successorOfClosestPrecedingNode);
            }
        }
    }

    private boolean withinYBounds(int dropY, Drawable node) {
        return dropY > node.y() - Tile.HALF_HEIGHT &&
                dropY < node.y() + Tile.HALF_HEIGHT;
    }
}
