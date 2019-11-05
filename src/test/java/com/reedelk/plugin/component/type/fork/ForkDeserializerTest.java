package com.reedelk.plugin.component.type.fork;

import com.reedelk.plugin.assertion.PluginAssertion;
import com.reedelk.plugin.graph.deserializer.AbstractNodeDeserializerTest;
import com.reedelk.plugin.graph.node.GraphNode;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.reedelk.plugin.fixture.Json.Fork;

class ForkDeserializerTest extends AbstractNodeDeserializerTest {

    private ForkDeserializer deserializer;

    @BeforeEach
    protected void setUp() {
        super.setUp();
        deserializer = new ForkDeserializer(graph, context);
    }

    @Test
    void shouldDeserializeForkDefinitionCorrectly() {
        // Given
        JSONObject forkDefinition = new JSONObject(Fork.Sample.json());

        // When
        GraphNode lastNode = deserializer.deserialize(root, forkDefinition);

        // Then
        PluginAssertion.assertThat(graph)
                .node(lastNode).is(stopNode1)
                .and().successorsOf(stopNode1).isEmpty()
                .and().successorsOf(forkNode1).areExactly(componentNode3, componentNode1)
                .and().successorsOf(componentNode3).isOnly(componentNode2)
                .and().successorsOf(componentNode1).isOnly(componentNode4)
                .and().successorsOf(componentNode2).isOnly(stopNode1)
                .and().successorsOf(componentNode4).isOnly(stopNode1)
                .and().nodesCountIs(7);
    }

}
