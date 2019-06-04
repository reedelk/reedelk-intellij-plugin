package com.esb.plugin.graph.node;

import com.esb.plugin.component.domain.ComponentData;
import com.esb.plugin.graph.FlowGraph;

import java.awt.*;
import java.awt.image.ImageObserver;

public class NothingSelectedNode implements GraphNode {

    @Override
    public void draw(FlowGraph graph, Graphics2D graphics, ImageObserver observer) {
        // nothing to draw
    }

    @Override
    public void drag(int x, int y) {

    }

    @Override
    public void dragging() {

    }

    @Override
    public void drop() {

    }

    @Override
    public void setPosition(int x, int y) {

    }

    @Override
    public void selected() {

    }

    @Override
    public void unselected() {

    }

    @Override
    public void drawArrows(FlowGraph graph, Graphics2D graphics, ImageObserver observer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    public int height(Graphics2D graphics) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int width(Graphics2D graphics) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(ImageObserver observer, int x, int y) {
        return false;
    }

    @Override
    public ComponentData componentData() {
        return null;
    }

}
