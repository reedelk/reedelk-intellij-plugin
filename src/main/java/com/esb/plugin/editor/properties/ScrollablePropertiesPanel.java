package com.esb.plugin.editor.properties;

import com.esb.plugin.editor.designer.SelectListener;
import com.esb.plugin.graph.FlowSnapshot;
import com.esb.plugin.graph.node.GraphNode;
import com.intellij.ui.components.JBScrollPane;

public class ScrollablePropertiesPanel extends JBScrollPane implements SelectListener {

    private PropertiesPanel propertiesPanel;

    public ScrollablePropertiesPanel() {
        super();
        this.propertiesPanel = new PropertiesPanel();
        setViewportView(propertiesPanel);
        createVerticalScrollBar();
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    @Override
    public void onSelect(FlowSnapshot snapshot, GraphNode node) {
        propertiesPanel.onSelect(snapshot, node);
    }

    @Override
    public void onUnselect() {
        propertiesPanel.onUnselect();
    }
}
