package com.esb.plugin.editor.designer.widget;

import com.esb.plugin.graph.FlowSnapshot;

import java.awt.*;

public class CenterOfNode {

    private final FlowSnapshot snapshot;

    public CenterOfNode(FlowSnapshot snapshot) {
        this.snapshot = snapshot;
    }

    public void draw(Graphics2D g2) {
        snapshot.getGraph()
                .breadthFirstTraversal(node ->
                        g2.drawOval(node.x() - 5, node.y() - 5, 10, 10));
    }
}
