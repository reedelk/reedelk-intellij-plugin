package com.reedelk.plugin.service.module.impl.component.completion;

import com.reedelk.plugin.service.module.impl.component.metadata.TypeProxy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.reedelk.plugin.service.module.impl.component.completion.Default.DEFAULT_RETURN_TYPE;
import static com.reedelk.runtime.api.commons.StringUtils.EMPTY;
import static com.reedelk.runtime.api.commons.StringUtils.isNotBlank;

public class TypeUtils {

    private static final String LIST_SIMPLE_NAME_FORMAT = "List<%s>";
    private static final String LIST_SIMPLE_NAME_AND_ITEM_TYPE_FORMAT = "%s : %s";
    private static final String FORMAT_LIST = "List<%s>";

    private TypeUtils() {
    }

    @NotNull
    public static String toSimpleName(@Nullable String type, @NotNull Trie typeTrie) {
        if (type == null) {
            return EMPTY;
        } else if (isNotBlank(typeTrie.displayName())) {
            return typeTrie.displayName();
        } else if (isNotBlank(typeTrie.listItemType())) {
            // If exists a list item type, it is a list and we want to display it with: List<ItemType>
            String listItemType = typeTrie.listItemType();
            return String.format(LIST_SIMPLE_NAME_FORMAT, TypeUtils.toSimpleName(listItemType));
        } else {
            return TypeUtils.toSimpleName(type);
        }
    }

    @NotNull
    public static String toSimpleName(@Nullable String type, @NotNull TypeAndTries allTypesMap) {
        Trie typeTrie = allTypesMap.getOrDefault(type);
        return TypeUtils.toSimpleName(type, typeTrie);
    }

    // Used by join
    public static String formatList(TypeProxy typeProxy, TypeAndTries typeAndTries) {
        String listItemType = TypeUtils.toSimpleName(typeProxy.listItemType(typeAndTries), typeAndTries);
        return String.format(FORMAT_LIST, listItemType);
    }

    @NotNull
    public static String formatUnrolledListDisplayType(TypeProxy typeProxy, TypeAndTries typeAndTries) {
        return String.format(LIST_SIMPLE_NAME_AND_ITEM_TYPE_FORMAT,
                typeProxy.toSimpleName(typeAndTries),
                TypeUtils.toSimpleName(typeProxy.listItemType(typeAndTries)));
    }

    @NotNull
    public static String returnTypeOrDefault(String type) {
        return isNotBlank(type) ? type : DEFAULT_RETURN_TYPE;
    }

    // Converts a fully qualified type name to a simple name, e.g: com.my.component.MyType > MyType.
    static String toSimpleName(String type) {
        if (type == null) return EMPTY;
        String[] splits = type.split(","); // might be multiple types
        List<String> tmp = new ArrayList<>();
        for (String split : splits) {
            String[] segments = split.split("\\.");
            tmp.add(segments[segments.length - 1]);
        }
        return String.join(",", tmp);
    }
}
