package com.reedelk.plugin.component.type.foreach;

import com.intellij.openapi.module.Module;
import com.reedelk.module.descriptor.model.component.ComponentOutputDescriptor;
import com.reedelk.module.descriptor.model.component.ComponentType;
import com.reedelk.plugin.graph.node.GraphNode;
import com.reedelk.plugin.service.module.PlatformModuleService;
import com.reedelk.plugin.service.module.impl.component.ComponentContext;
import com.reedelk.plugin.service.module.impl.component.completion.Default;
import com.reedelk.plugin.service.module.impl.component.completion.Trie;
import com.reedelk.plugin.service.module.impl.component.completion.TypeAndTries;
import com.reedelk.plugin.service.module.impl.component.metadata.AbstractDiscoveryStrategy;
import com.reedelk.plugin.service.module.impl.component.metadata.MultipleMessages;
import com.reedelk.runtime.api.commons.StringUtils;
import com.reedelk.runtime.api.message.MessageAttributes;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;

public class ForEachComponentDiscovery extends AbstractDiscoveryStrategy {

    public ForEachComponentDiscovery(Module module, PlatformModuleService moduleService, TypeAndTries typeAndAndTries) {
        super(module, moduleService, typeAndAndTries);
    }

    // TODO: Bug: For each followed by fork, displays List instead of Object
    @Override
    public Optional<ComponentOutputDescriptor> compute(ComponentContext context, GraphNode nodeWeWantOutputFrom) {
        Optional<? extends ComponentOutputDescriptor> componentOutputDescriptor = discover(context, nodeWeWantOutputFrom);
        if (componentOutputDescriptor.isPresent()) {
            ComponentOutputDescriptor previousComponentOutput = componentOutputDescriptor.get();
            List<String> payload = previousComponentOutput.getPayload();
            if (payload != null && payload.size() == 1) {
                String precedingPayloadType = payload.get(0);
                Trie orDefault = typeAndAndTries.getOrDefault(precedingPayloadType, Default.UNKNOWN);
                ComponentOutputDescriptor descriptor = new ComponentOutputDescriptor();
                descriptor.setAttributes(previousComponentOutput.getAttributes());
                if (StringUtils.isNotBlank(orDefault.listItemType())) {
                    descriptor.setPayload(singletonList(orDefault.listItemType()));
                } else {
                    descriptor.setPayload(singletonList(precedingPayloadType));
                }
                return Optional.of(descriptor);
            }
        }

        ComponentOutputDescriptor descriptor = new ComponentOutputDescriptor();
        descriptor.setPayload(singletonList(Object.class.getName()));
        descriptor.setAttributes(singletonList(MessageAttributes.class.getName()));
        return Optional.of(descriptor);
    }

    @Override
    public Optional<? extends ComponentOutputDescriptor> compute(ComponentContext context, Collection<GraphNode> predecessors) {
        ComponentType componentClass = context.node().getComponentType();
        if (ComponentType.JOIN.equals(componentClass)) {
            MultipleMessages descriptor = new MultipleMessages();
            return Optional.of(descriptor);
        } else {
            ComponentOutputDescriptor outputDescriptor = new ComponentOutputDescriptor();
            outputDescriptor.setPayload(singletonList(List.class.getName()));
            outputDescriptor.setAttributes(singletonList(MessageAttributes.class.getName()));
            return Optional.of(outputDescriptor);
        }
    }
}
