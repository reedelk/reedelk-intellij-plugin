package com.reedelk.plugin.editor.designer.hint.strategy;

import com.reedelk.plugin.commons.IsPlaceholderNode;
import com.reedelk.plugin.commons.IsScopedGraphNode;
import com.reedelk.plugin.editor.designer.hint.HintResult;
import com.reedelk.plugin.graph.FlowGraph;
import com.reedelk.plugin.graph.node.GraphNode;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.ImageObserver;

public class HintIsPredecessorOfPlaceholderNode extends BaseStrategy {

    @Override
    public boolean applicable(@NotNull FlowGraph graph,
                              @NotNull HintResult hintResult,
                              @NotNull Graphics2D g2,
                              @NotNull ImageObserver imageObserver) {

        GraphNode hintNode = hintResult.getHintNode();
        Point hintPoint = hintResult.getHintPoint();

        return IsScopedGraphNode.of(hintNode) &&
                graph.successors(hintNode)
                    .stream()
                    .filter(IsPlaceholderNode::of)
                    .anyMatch(placeholder -> placeholder.contains(imageObserver, hintPoint.x, hintPoint.y));
    }

    @Override
    public void draw(@NotNull FlowGraph graph,
                     @NotNull HintResult hintResult,
                     @NotNull Graphics2D g2,
                     @NotNull ImageObserver imageObserver) {
        GraphNode hintNode = hintResult.getHintNode();

        Point hintPoint = hintResult.getHintPoint();
        graph.successors(hintNode)
                .stream()
                .filter(IsPlaceholderNode::of)
                .filter(placeholder -> placeholder.contains(imageObserver, hintPoint.x, hintPoint.y))
                .findFirst()
                .ifPresent(node -> drawPlaceholderHint(g2, node, imageObserver));
    }
}
