package com.esb.plugin.designer.properties;

import com.esb.plugin.graph.node.GraphNode;
import com.intellij.ui.components.JBPanel;

public interface PropertiesRenderer {

    JBPanel render(GraphNode node);

}
