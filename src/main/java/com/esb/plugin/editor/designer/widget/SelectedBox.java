package com.esb.plugin.editor.designer.widget;

import com.esb.plugin.commons.Half;
import com.esb.plugin.graph.node.GraphNode;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class SelectedBox {

    private static final int TOP_PADDING = 4;
    private static final int BOTTOM_PADDING = 8;
    private static final int RIGHT_PADDING = 2;
    private static final Color SELECTED_COMPONENT_BG_COLOR = new JBColor(Gray._245, Gray._245);
    protected int x;
    protected int y;

    public void draw(GraphNode node, Graphics2D graphics) {
        int parentWidth = node.width(graphics);
        int bottomHalfHeight = node.bottomHalfHeight(graphics);
        graphics.setColor(SELECTED_COMPONENT_BG_COLOR);

        RoundRectangle2D.Double aDouble = new RoundRectangle2D.Double(
                x - Half.of(parentWidth),
                y + TOP_PADDING, parentWidth - RIGHT_PADDING, bottomHalfHeight - BOTTOM_PADDING, 10, 10);

        graphics.fill(aDouble);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
