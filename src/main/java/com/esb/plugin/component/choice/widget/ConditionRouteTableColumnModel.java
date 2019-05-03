package com.esb.plugin.component.choice.widget;

import com.esb.plugin.graph.node.GraphNode;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

import static com.esb.plugin.designer.canvas.drawables.ComponentDescription.DESCRIPTION_PROPERTY_NAME;

class ConditionRouteTableColumnModel extends DefaultTableColumnModel {

    ConditionRouteTableColumnModel(JComboBox routesComboBox) {
        // Column 0
        TableColumn conditionColumn = new TableColumn(0);
        conditionColumn.setHeaderValue(ConditionRouteColumns.COLUMN_NAMES[0]);
        addColumn(conditionColumn);

        // Column 1
        TableColumn routeColumn = new TableColumn(1);
        routeColumn.setHeaderValue(ConditionRouteColumns.COLUMN_NAMES[1]);
        routeColumn.setCellEditor(new DefaultCellEditor(routesComboBox));
        routeColumn.setCellRenderer(new RoutesCellRenderer());

        addColumn(routeColumn);
    }

    class RoutesCellRenderer extends DefaultTableCellRenderer {
        @Override
        protected void setValue(Object value) {
            GraphNode node = (GraphNode) value;
            String description = (String) node.component().getData(DESCRIPTION_PROPERTY_NAME);
            setText(description);
        }
    }

}
