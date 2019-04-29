package com.esb.plugin.designer.graph.layout;

import com.esb.plugin.designer.graph.FlowGraph;
import com.esb.plugin.designer.graph.GraphNode;
import com.esb.plugin.designer.graph.drawable.ScopedDrawable;
import com.esb.plugin.designer.graph.scope.CountNestedScopes;
import com.esb.plugin.designer.graph.scope.FindFirstNodeOutsideScope;

import java.awt.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.esb.plugin.designer.graph.drawable.ScopedDrawable.HORIZONTAL_PADDING;
import static com.esb.plugin.designer.graph.drawable.ScopedDrawable.VERTICAL_PADDING;
import static com.google.common.base.Preconditions.checkState;

public class FlowGraphLayoutUtils {

    static int computeMaxHeight(FlowGraph graph, Graphics2D graphics, GraphNode start) {
        return computeMaxHeight(graph, graphics, start, null);
    }

    public static int computeMaxHeight(FlowGraph graph, Graphics2D graphics, GraphNode start, GraphNode end) {
        return computeMaxHeight(graphics, graph, start, end, 0);
    }

    private static int computeMaxHeight(Graphics2D graphics, FlowGraph graph, GraphNode start, GraphNode end, int currentMax) {
        if (start == end) {
            return currentMax;

        } else if (start instanceof ScopedDrawable) {
            return computeMaxHeight(graphics, graph, (ScopedDrawable) start, end, currentMax);

        } else {

            int newMax = currentMax > start.height(graphics) ? currentMax : start.height(graphics);
            List<GraphNode> successors = graph.successors(start);
            checkState(successors.size() == 1 || successors.isEmpty(),
                    "Zero or at most one successors expected");

            if (successors.isEmpty()) {
                return newMax;
            }

            GraphNode successor = successors.get(0);
            return computeMaxHeight(graphics, graph, successor, end, newMax);
        }
    }

    private static int computeMaxHeight(Graphics2D graphics, FlowGraph graph, ScopedDrawable scopedDrawable, GraphNode end, int currentMax) {
        List<GraphNode> successors = graph.successors(scopedDrawable);

        GraphNode firstNodeOutsideScope = FindFirstNodeOutsideScope.of(graph, scopedDrawable).orElse(null);

        int sum = VERTICAL_PADDING + VERTICAL_PADDING;
        for (GraphNode successor : successors) {
            // We are looking for the max in the subtree starting from this successor.
            // Therefore the current max starts again from 0.
            sum += computeMaxHeight(graphics, graph, successor, firstNodeOutsideScope, 0);
        }

        // If this scope does not have successors, the sum is its height.
        if (successors.isEmpty()) {
            sum += scopedDrawable.height(graphics);
        }

        int followingMax = 0;
        if (firstNodeOutsideScope != end && firstNodeOutsideScope != null) {
            followingMax = computeMaxHeight(graphics, graph, firstNodeOutsideScope, null, 0);
        }


        int newCurrentMax = sum > currentMax ? sum : currentMax;

        return followingMax > newCurrentMax ? followingMax : newCurrentMax;
    }


    static int findContainingLayer(List<List<GraphNode>> layers, GraphNode current) {
        for (int i = 0; i < layers.size(); i++) {
            if (layers.get(i).contains(current)) {
                return i;
            }
        }
        throw new RuntimeException("Could not find containing layer for node " + current);
    }

    static int layerWidthSumPreceding(FlowGraph graph, Graphics2D graphics, List<List<GraphNode>> layers, int precedingLayerIndex) {
        int sum = 0;
        for (int i = 0; i < precedingLayerIndex; i++) {
            List<GraphNode> layerDrawables = layers.get(i);
            sum += maxLayerWidth(graph, graphics, layerDrawables);
        }
        return sum;
    }

    static GraphNode findCommonParent(FlowGraph graph, Collection<GraphNode> drawables) {
        Set<GraphNode> commonParents = new HashSet<>();
        drawables.forEach(drawable -> commonParents.addAll(graph.predecessors(drawable)));
        checkState(commonParents.size() == 1, "Common parent must be one");
        return commonParents.stream().findFirst().get();
    }

    private static int maxLayerWidth(FlowGraph graph, Graphics2D graphics, List<GraphNode> layerDrawables) {
        int max = 0;
        for (GraphNode layerDrawable : layerDrawables) {
            int nestedScopes = CountNestedScopes.of(graph, layerDrawable);
            int total = layerDrawable.width(graphics) + nestedScopes * HORIZONTAL_PADDING;
            if (total > max) max = total;
        }
        return max;
    }
}
