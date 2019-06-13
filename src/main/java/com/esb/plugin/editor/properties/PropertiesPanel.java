package com.esb.plugin.editor.properties;

import com.esb.plugin.component.domain.ComponentData;
import com.esb.plugin.editor.designer.SelectListener;
import com.esb.plugin.editor.properties.renderer.node.NodePropertiesRendererFactory;
import com.esb.plugin.graph.FlowSnapshot;
import com.esb.plugin.graph.node.GraphNode;
import com.esb.plugin.graph.node.NothingSelectedNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTabbedPane;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

import static java.awt.BorderLayout.*;

public class PropertiesPanel extends JBPanel implements SelectListener {

    private final MatteBorder border = BorderFactory.createMatteBorder(0, 10, 0, 0, getBackground());

    private final Module module;
    private final FlowSnapshot snapshot;

    public PropertiesPanel(Module module, FlowSnapshot snapshot) {
        this.module = module;
        this.snapshot = snapshot;
        setBorder(border);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    }

    @Override
    public void onSelect(FlowSnapshot snapshot, GraphNode node) {
        if (node instanceof NothingSelectedNode) return;

        ComponentData componentData = node.componentData();
        removeAll();

        JBPanel propertiesPanel = createPropertiesPanel(componentData, snapshot, node);

        Icon icon = componentData.getComponentIcon();

        JBTabbedPane tabbedPane = new JBTabbedPane();
        tabbedPane.addTab(componentData.getDisplayName(), icon, propertiesPanel, componentData.getDisplayName() + " properties");

        add(tabbedPane);

        revalidate();
        repaint();
    }

    private static final Logger LOG = Logger.getInstance(PropertiesPanel.class);

    private JLabel label = new JLabel();

    @Override
    public void onUnselect() {
        removeAll();
        String description = snapshot.getGraph().description();
        label.setText(description);
        LOG.warn("Description: " + description);
        add(label);
        revalidate();
        repaint();
    }

    private JBPanel createPropertiesPanel(ComponentData componentData, FlowSnapshot snapshot, GraphNode node) {
        JBPanel propertiesPanel = NodePropertiesRendererFactory.get()
                .component(componentData)
                .snapshot(snapshot)
                .module(module)
                .build()
                .render(node);

        JBPanel propertiesBoxContainer = createPropertiesBoxPanel(propertiesPanel);
        JBPanel inputOutputPanel = createInputOutputPanel();
        return createPropertiesHolder(propertiesBoxContainer, inputOutputPanel);
    }

    private JBPanel createInputOutputPanel() {
        JBLabel inputOutputLabel = new JBLabel("input/output");
        JBPanel inputOutputPanel = new JBPanel();
        inputOutputPanel.setPreferredSize(new Dimension(300, 100));
        inputOutputPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, JBColor.LIGHT_GRAY));
        inputOutputPanel.add(inputOutputLabel, CENTER);
        return inputOutputPanel;
    }

    private JBPanel createPropertiesHolder(JBPanel propertiesBoxContainer, JBPanel inputOutputPanel) {
        JBPanel propertiesHolder = new JBPanel();
        propertiesHolder.setLayout(new BorderLayout());
        propertiesHolder.add(propertiesBoxContainer, CENTER);
        propertiesHolder.add(inputOutputPanel, EAST);
        return propertiesHolder;
    }

    private JBPanel createPropertiesBoxPanel(JBPanel propertiesListPanel) {
        JBPanel fillerPanel = new JBPanel();
        fillerPanel.add(Box.createGlue());

        JBPanel propertiesBoxContainer = new JBPanel();
        propertiesBoxContainer.setLayout(new BorderLayout());
        propertiesBoxContainer.add(propertiesListPanel, NORTH);
        propertiesBoxContainer.add(fillerPanel, CENTER);
        return propertiesBoxContainer;
    }

}
