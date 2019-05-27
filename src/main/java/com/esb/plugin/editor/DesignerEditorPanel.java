package com.esb.plugin.editor;

import com.esb.plugin.editor.properties.ScrollablePropertiesPanel;
import com.esb.plugin.graph.GraphSnapshot;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.ui.ThreeComponentsSplitter;

import javax.swing.event.AncestorListener;

public class DesignerEditorPanel extends ThreeComponentsSplitter {

    private static final int PROPERTIES_PANEL_SIZE = 200;
    private static final boolean VERTICAL = true;

    DesignerEditorPanel(Module module, GraphSnapshot snapshot, AncestorListener listener) {
        super(VERTICAL);

        ScrollablePropertiesPanel propertiesPanel = new ScrollablePropertiesPanel();
        DesignerAndPalettePanel designerAndPalettePanel = new DesignerAndPalettePanel(module, snapshot, propertiesPanel, listener);

        setInnerComponent(designerAndPalettePanel);
        setLastComponent(propertiesPanel);
        setLastSize(PROPERTIES_PANEL_SIZE);
    }
}
