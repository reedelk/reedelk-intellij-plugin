package com.esb.plugin.designer.graph.drawable.decorators;

import com.esb.plugin.designer.Tile;
import com.esb.plugin.designer.graph.FlowGraph;
import com.esb.plugin.designer.graph.drawable.Drawable;

import java.awt.*;
import java.awt.image.ImageObserver;

public class ArrowsDrawable extends AbstractDrawable {

    private final Drawable parent;
    private int x;
    private int y;

    public ArrowsDrawable(Drawable parent) {
        this.parent = parent;
    }

    @Override
    public void draw(FlowGraph graph, Graphics2D graphics, ImageObserver observer) {
        graph.successors(parent).forEach(successor -> {

            Point sourceBaryCenter = parent.getBarycenter(graphics);
            Point source = new Point(
                    sourceBaryCenter.x + Math.floorDiv(Tile.WIDTH, 2) - 15,
                    sourceBaryCenter.y);

            Point targetBaryCenter = successor.getBarycenter(graphics);
            Point target = new Point(
                    targetBaryCenter.x - Math.floorDiv(Tile.WIDTH, 2) + 15,
                    targetBaryCenter.y);

            Arrow arrow = new Arrow(source, target);
            arrow.draw(graphics);

        });
    }

    @Override
    public int y() {
        return y;
    }

    @Override
    public int x() {
        return x;
    }

    @Override
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}


