package com.reedelk.plugin.component.type.fork;

import com.intellij.openapi.module.Module;
import com.reedelk.module.descriptor.model.component.ComponentOutputDescriptor;
import com.reedelk.module.descriptor.model.component.ComponentType;
import com.reedelk.plugin.component.type.generic.GenericComponentDiscovery;
import com.reedelk.plugin.graph.node.GraphNode;
import com.reedelk.plugin.service.module.PlatformModuleService;
import com.reedelk.plugin.service.module.impl.component.ComponentContext;
import com.reedelk.plugin.service.module.impl.component.completion.TypeAndTries;
import com.reedelk.plugin.service.module.impl.component.metadata.MultipleMessages;
import com.reedelk.runtime.api.message.MessageAttributes;

import java.util.*;
import java.util.function.Consumer;

public class ForkComponentDiscovery extends GenericComponentDiscovery {

    public ForkComponentDiscovery(Module module, PlatformModuleService moduleService, TypeAndTries typeAndAndTries) {
        super(module, moduleService, typeAndAndTries);
    }

    @Override
    public Optional<? extends ComponentOutputDescriptor> compute(ComponentContext context, GraphNode currentNode) {
        // Skip one
        return discover(context, currentNode);
    }

    @Override
    public Optional<? extends ComponentOutputDescriptor> compute(ComponentContext context, Collection<GraphNode> predecessors) {
        ComponentType componentClass = context.node().getComponentType();
        if (ComponentType.JOIN.equals(componentClass)) {
            MultipleMessages descriptor = new MultipleMessages();
            return Optional.of(descriptor);
        } else {
            // For each predecessor we should compute the component output descriptor... and merge all the attributes
            Set<String> attributes = new HashSet<>();
            Set<String> payloads = new HashSet<>();
            for (GraphNode node : predecessors) {
                discover(context, node).ifPresent((Consumer<ComponentOutputDescriptor>) componentOutputDescriptor -> {
                    // Add all the attributes
                    String predecessorAttributes = componentOutputDescriptor.getAttributes();
                    attributes.add(predecessorAttributes);

                    // Add all the payloads
                    List<String> predecessorPayload = componentOutputDescriptor.getPayload();
                    payloads.addAll(predecessorPayload);
                });
            }
            ComponentOutputDescriptor outputDescriptor = new ComponentOutputDescriptor();

            if (attributes.isEmpty()) {
                outputDescriptor.setAttributes(MessageAttributes.class.getName());
            } else {
                outputDescriptor.setAttributes(String.join(",", attributes));
            }

            outputDescriptor.setPayload(Collections.singletonList(List.class.getName()));

            return Optional.of(outputDescriptor);
        }
    }
}
