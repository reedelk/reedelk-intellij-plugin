package com.esb.plugin.component.type.router;

import com.esb.plugin.commons.Half;
import com.esb.plugin.commons.Labels;
import com.esb.plugin.component.domain.ComponentData;
import com.esb.plugin.component.type.router.functions.IsDefaultRoute;
import com.esb.plugin.component.type.router.functions.ListConditionRoutePairs;
import com.esb.plugin.component.type.router.functions.SyncConditionAndRoutePairs;
import com.esb.plugin.editor.designer.AbstractScopedGraphNode;
import com.esb.plugin.editor.designer.DrawableListener;
import com.esb.plugin.editor.designer.widget.Icon;
import com.esb.plugin.editor.designer.widget.VerticalDivider;
import com.esb.plugin.editor.designer.widget.VerticalDividerArrows;
import com.esb.plugin.graph.FlowGraph;
import com.esb.plugin.graph.node.GraphNode;
import com.esb.plugin.graph.node.GraphNodeFactory;
import com.esb.plugin.graph.node.ScopedGraphNode;
import com.esb.system.component.Placeholder;
import com.esb.system.component.Router;
import com.intellij.openapi.module.Module;
import com.intellij.ui.JBColor;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;

public class RouterNode extends AbstractScopedGraphNode {

    public static final String DATA_CONDITION_ROUTE_PAIRS = "conditionRoutePairs";
    public static final int HEIGHT = 140;
    private static final int WIDTH = 170;

    private static final int VERTICAL_DIVIDER_X_OFFSET = 60;
    private static final int ICON_X_OFFSET = 30;


    private final Icon icon;
    private final VerticalDivider verticalDivider;
    private final VerticalDividerArrows verticalDividerArrows;

    public RouterNode(ComponentData componentData) {
        super(componentData);
        this.icon = new Icon(componentData);
        this.verticalDivider = new VerticalDivider(this);
        this.verticalDividerArrows =
                new VerticalDividerArrows(VERTICAL_DIVIDER_X_OFFSET, new RouterOnProcessSuccessor());
    }

    @Override
    public void draw(FlowGraph graph, Graphics2D graphics, ImageObserver observer) {
        super.draw(graph, graphics, observer);
        icon.draw(graphics, observer);
        verticalDivider.draw(graph, graphics, observer);
    }

    @Override
    public void mouseMoved(DrawableListener listener, MouseEvent event) {
        int x = event.getX();
        int y = event.getY();
        if (icon.contains(x, y)) {
            listener.setTheCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }

    @Override
    public void mousePressed(DrawableListener listener, MouseEvent event) {
        // Nothing to do
    }

    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y);
        icon.setPosition(x - ICON_X_OFFSET, y);
        verticalDivider.setPosition(x - VERTICAL_DIVIDER_X_OFFSET, y);
    }

    @Override
    public void drawArrows(FlowGraph graph, Graphics2D graphics, ImageObserver observer) {
        super.drawArrows(graph, graphics, observer);
        verticalDividerArrows.draw(this, graph, graphics);
    }

    @Override
    public void drawDrag(FlowGraph graph, Graphics2D graphics, ImageObserver observer) {

    }

    @Override
    public Point getTargetArrowEnd() {
        return icon.getTargetArrowEnd();
    }

    @Override
    public Point getSourceArrowStart() {
        return icon.getSourceArrowStart();
    }

    @Override
    public boolean contains(ImageObserver observer, int x, int y) {
        return icon.contains(x, y);
    }

    @Override
    public int bottomHalfHeight(Graphics2D graphics) {
        return Half.of(HEIGHT);
    }

    @Override
    public int topHalfHeight(Graphics2D graphics) {
        return Half.of(HEIGHT);
    }

    @Override
    public int height(Graphics2D graphics) {
        return HEIGHT;
    }

    @Override
    public int width(Graphics2D graphics) {
        return WIDTH;
    }

    @Override
    public boolean isSuccessorAllowed(FlowGraph graph, GraphNode successor, int index) {
        List<GraphNode> successors = graph.successors(this);
        return index < successors.size();
    }

    @Override
    public void commit(FlowGraph graph, Module module) {
        // If successors is empty, lets add a placeholder

        List<RouterConditionRoutePair> routerConditionRoutePairs =
                ListConditionRoutePairs.of(componentData());

        if (getScope().isEmpty()) {
            GraphNode placeholder = GraphNodeFactory.get(module, Placeholder.class.getName());
            graph.add(placeholder);
            List<GraphNode> successors = graph.successors(this);
            List<GraphNode> toRemove = new ArrayList<>(successors);
            toRemove.forEach(node -> {
                graph.remove(RouterNode.this, node);
                graph.add(placeholder, node);
            });
            graph.add(this, placeholder);
            addToScope(placeholder);

            routerConditionRoutePairs.add(new RouterConditionRoutePair(Router.DEFAULT_CONDITION, placeholder));
        }

        List<RouterConditionRoutePair> updatedConditions =
                SyncConditionAndRoutePairs.getUpdatedPairs(graph, this, routerConditionRoutePairs);
        ComponentData component = componentData();
        component.set(DATA_CONDITION_ROUTE_PAIRS, updatedConditions);
    }

    class RouterOnProcessSuccessor implements VerticalDividerArrows.OnProcessSuccessor {

        private final int DEFAULT_ROUTE_TEXT_LEFT_PADDING = 6;
        private final int DEFAULT_ROUTE_TEXT_TOP_PADDING = 14;

        @Override
        public void onProcess(ScopedGraphNode parent, GraphNode successor, Graphics2D graphics) {
            if (IsDefaultRoute.of(parent, successor)) {
                int halfWidth = Half.of(parent.width(graphics));
                int verticalX = parent.x() - VERTICAL_DIVIDER_X_OFFSET + halfWidth;
                graphics.setColor(JBColor.GRAY);
                Point targetArrowEnd = successor.getTargetArrowEnd();
                graphics.drawString(Labels.ROUTER_DEFAULT_ROUTE, verticalX + DEFAULT_ROUTE_TEXT_LEFT_PADDING,
                        targetArrowEnd.y + DEFAULT_ROUTE_TEXT_TOP_PADDING);
            }
        }
    }
}
