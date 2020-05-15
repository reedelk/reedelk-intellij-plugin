package com.reedelk.plugin.service.module.impl.component.completion;

import com.reedelk.module.descriptor.model.component.ComponentOutputDescriptor;
import com.reedelk.runtime.api.message.MessageAttributes;
import com.reedelk.runtime.api.message.MessagePayload;

import java.util.Collection;
import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class TypeDynamicUtils {

    private TypeDynamicUtils() {
    }

    // We need to create an artificial suggestion for each dynamic type found.
    // We keep the original suggestion parameters, we only change the return type and its display value.
    // The dynamic types depend on the previous component output descriptor.
    // Note that there might be multiple dynamic types because a component
    // could have multiple outputs such as RESTListener -> output String,byte[] or Map (attachments for multipart).
    public static Collection<Suggestion> createDynamicSuggestions(ComponentOutputDescriptor descriptor,
                                                                  Suggestion suggestion,
                                                                  TypeAndTries typeAndTrieMap) {
        return TypeDynamicUtils.resolve(suggestion, descriptor)
                .stream()
                .map(dynamicType -> Suggestion.create(suggestion.getType())
                        .cursorOffset(suggestion.getCursorOffset())
                        .insertValue(suggestion.getInsertValue())
                        .lookupToken(suggestion.getLookupToken())
                        .tailText(suggestion.getTailText())
                        .returnTypeDisplayValue(TypeUtils.toSimpleName(dynamicType, typeAndTrieMap, suggestion))
                        .returnType(dynamicType)
                        .build())
                .collect(toList());
    }

    public static boolean is(Suggestion suggestion) {
        return MessagePayload.class.getName().equals(suggestion.getReturnType()) ||
                MessageAttributes.class.getName().equals(suggestion.getReturnType());
    }

    // Resolves the dynamic type from the output descriptor
    static List<String> resolve(Suggestion suggestion, ComponentOutputDescriptor descriptor) {
        String suggestionType = suggestion.getReturnType();
        if (MessageAttributes.class.getName().equals(suggestionType)) {
            return descriptor != null && descriptor.getAttributes() != null ?
                    descriptor.getAttributes() :
                    singletonList(MessageAttributes.class.getName());

        } else if (MessagePayload.class.getName().equals(suggestionType)) {
            return descriptor != null && descriptor.getPayload() != null ?
                    descriptor.getPayload() :
                    singletonList(Object.class.getName());

        } else {
            throw new IllegalStateException("Resolve must be called only if the suggestion type is dynamic");
        }
    }
}
