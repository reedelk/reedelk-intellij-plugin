package com.reedelk.plugin.component.type.flowreference.discovery;

import com.reedelk.plugin.AbstractGraphTest;
import com.reedelk.plugin.graph.FlowGraph;
import com.reedelk.plugin.graph.node.GraphNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SubflowReferenceGraphAwareContextTest extends AbstractGraphTest {

    private SubflowReferenceGraphAwareContext context;
    private FlowGraph subflowGraph;

    @BeforeEach
    public void setUp() {
        super.setUp();
        subflowGraph = provider.createGraph();
        subflowGraph.root(root);
        subflowGraph.add(root, componentNode1);
        subflowGraph.add(componentNode1, componentNode2);
        subflowGraph.add(componentNode2, componentNode3);

        context = new SubflowReferenceGraphAwareContext(subflowGraph, flowReferenceNode1);
    }

    @Test
    void shouldReturnEndNodesOfSubflowGraphWhenTargetIsFlowReference() {
        // When
        List<GraphNode> predecessors = context.predecessors(flowReferenceNode1);

        // Then
        assertThat(predecessors).contains(componentNode3);
    }

    @Test
    void shouldReturnCorrectPredecessorOfSubflowGraphNode() {
        // When
        List<GraphNode> predecessors = context.predecessors(componentNode3);

        // Then
        assertThat(predecessors).contains(componentNode2);
    }
}
