package com.esb.plugin.component.type.placeholder;

import com.esb.plugin.component.domain.ComponentData;
import com.esb.plugin.editor.designer.AbstractGraphNode;
import com.esb.plugin.editor.designer.DrawableListener;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;

public class PlaceholderNode extends AbstractGraphNode {

    public PlaceholderNode(ComponentData componentData) {
        super(componentData);
    }

    @Override
    protected void drawRemoveComponentIcon(Graphics2D graphics, ImageObserver observer) {
        // Placeholder cannot be removed
    }

    @Override
    public void mouseMoved(DrawableListener listener, MouseEvent event) {
        int x = event.getX();
        int y = event.getY();
        if (icon.contains(x, y)) {
            listener.setTheCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }
}
