package com.esb.plugin.editor;

import com.esb.plugin.editor.designer.DesignerPanel;
import com.esb.plugin.editor.designer.DesignerPanelActionHandler;
import com.esb.plugin.editor.designer.ScrollableDesignerPanel;
import com.esb.plugin.editor.properties.PropertiesPanel;
import com.esb.plugin.graph.FlowSnapshot;
import com.intellij.openapi.ui.ThreeComponentsSplitter;

public class DesignerEditor extends ThreeComponentsSplitter {

    private static final int PROPERTIES_PANEL_SIZE = 230;
    private static final boolean VERTICAL = true;

    DesignerEditor(FlowSnapshot snapshot, DesignerPanelActionHandler actionHandler, PropertiesPanel propertiesPanel) {
        super(VERTICAL);
        DesignerPanel canvas = new DesignerPanel(snapshot, actionHandler);
        canvas.addListener(propertiesPanel);
        ScrollableDesignerPanel canvasPanel = new ScrollableDesignerPanel(canvas);

        setInnerComponent(canvasPanel);
        setLastComponent(propertiesPanel);
        setLastSize(PROPERTIES_PANEL_SIZE);
    }
}
