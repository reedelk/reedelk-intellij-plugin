package com.esb.plugin.editor.properties.widget;

import com.intellij.openapi.Disposable;
import com.intellij.ui.components.JBPanel;

import java.awt.*;

public class DisposablePanel extends JBPanel implements Disposable {

    public DisposablePanel() {
        super();
    }

    public DisposablePanel(LayoutManager layoutManager) {
        super(layoutManager);
    }

    @Override
    public void dispose() {
        // Dispose all the children components implementing
        // the disposable interface. This is for instance needed
        // to correctly release resources of Javascript Editor.
        Component[] components = getComponents();
        for (Component component : components) {
            if (component instanceof Disposable) {
                ((Disposable) component).dispose();
            }
        }
    }
}
