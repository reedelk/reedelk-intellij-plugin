package com.esb.plugin.graph.deserializer;

import com.esb.plugin.component.ComponentData;
import com.esb.plugin.component.ComponentDescriptor;
import com.esb.plugin.graph.FlowGraph;
import com.esb.plugin.graph.FlowGraphImpl;
import com.esb.plugin.graph.node.GraphNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractBuilderTest {

    @Mock
    protected GraphNode root;
    @Mock
    protected DeserializerContext context;

    protected FlowGraphImpl graph;

    @BeforeEach
    protected void setUp() {
        graph = new FlowGraphImpl();
        graph.root(root);
    }

    protected void assertThatComponentHasName(GraphNode target, String expectedName) {
        assertThat(target).isNotNull();
        ComponentData componentData = target.component();
        assertThat(componentData).isNotNull();
        assertThat(componentData.getFullyQualifiedName()).isEqualTo(expectedName);
    }

    protected void assertSuccessorsAre(FlowGraph graph, GraphNode target, String... successorsComponentNames) {
        int numberOfSuccessors = successorsComponentNames.length;
        List<GraphNode> successors = graph.successors(target);
        assertThat(successors).isNotNull().hasSize(numberOfSuccessors);

        Collection<String> toBeFound = new ArrayList<>(Arrays.asList(successorsComponentNames));
        successors.forEach(successor -> {
            String componentName = successor.component().getFullyQualifiedName();
            toBeFound.remove(componentName);
        });

        assertThat(toBeFound).isEmpty();
    }

    protected void assertPredecessorsAre(FlowGraph graph, GraphNode target, String... predecessorsComponentsNames) {
        int numberOfPredecessors = predecessorsComponentsNames.length;

        List<GraphNode> predecessors = graph.predecessors(target);
        assertThat(predecessors).isNotNull().hasSize(numberOfPredecessors);

        Collection<String> toBeFound = new ArrayList<>(Arrays.asList(predecessorsComponentsNames));
        predecessors.forEach(predecessor -> {
            String componentName = predecessor.component().getFullyQualifiedName();
            toBeFound.remove(componentName);
        });

        assertThat(toBeFound).isEmpty();
    }

    protected GraphNode firstSuccessorOf(FlowGraph graph, GraphNode target) {
        return graph.successors(target).stream().findFirst().get();
    }

    protected GraphNode getNodeHavingComponentName(Collection<GraphNode> drawables, String componentName) {
        for (GraphNode drawable : drawables) {
            ComponentData componentData = drawable.component();
            if (componentName.equals(componentData.getFullyQualifiedName())) {
                return drawable;
            }
        }
        throw new RuntimeException("Could not find: " + componentName);
    }

    protected ComponentData mockComponent(String fullyQualifiedName, Class<? extends GraphNode> nodeClazz) {
        ComponentData componentData = new ComponentData(ComponentDescriptor.create()
                .fullyQualifiedName(fullyQualifiedName)
                .build());
        try {
            GraphNode node = nodeClazz.getConstructor(ComponentData.class).newInstance(componentData);
            doReturn(node)
                    .when(context)
                    .instantiateGraphNode(fullyQualifiedName);
            return componentData;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}
