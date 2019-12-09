package com.reedelk.plugin.service.module.impl.completion;

import com.intellij.openapi.compiler.CompilationStatusListener;
import com.intellij.openapi.compiler.CompilerTopics;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import com.reedelk.plugin.commons.SuggestionDefinitionMatcher;
import com.reedelk.plugin.component.domain.AutoCompleteContributorDefinition;
import com.reedelk.plugin.executor.PluginExecutor;
import com.reedelk.plugin.message.SuggestionsBundle;
import com.reedelk.plugin.service.module.CompletionService;
import com.reedelk.plugin.service.module.ComponentService;
import com.reedelk.plugin.service.module.impl.component.ComponentsPackage;
import com.reedelk.plugin.service.module.impl.component.scanner.ComponentListUpdateNotifier;
import com.reedelk.plugin.topic.ReedelkTopics;
import com.reedelk.runtime.api.commons.StringUtils;

import java.util.*;

import static com.reedelk.plugin.topic.ReedelkTopics.COMPLETION_EVENT_TOPIC;
import static java.util.stream.Collectors.toList;

public class CompletionServiceImpl implements CompletionService, CompilationStatusListener, ComponentListUpdateNotifier {

    private final Trie defaultComponentTrie;
    private Trie customFunctionsTrie;
    private final Module module;

    private final Map<String, Trie> componentTriesMap = new HashMap<>();
    private OnCompletionEvent onCompletionEvent;

    // Custom Functions are global so they are always present.
    // Need to define default script suggestions and specific suggestions for
    // Component by module, and component fully qualified name and property.
    public CompletionServiceImpl(Project project, Module module) {
        this.module = module;

        MessageBus messageBus = project.getMessageBus();

        MessageBusConnection connection = messageBus.connect();
        connection.subscribe(CompilerTopics.COMPILATION_STATUS, this);
        connection.subscribe(ReedelkTopics.COMPONENTS_UPDATE_EVENTS, this);
        this.onCompletionEvent = messageBus.syncPublisher(COMPLETION_EVENT_TOPIC);

        this.defaultComponentTrie = new Trie();
        this.customFunctionsTrie = new Trie();
        PluginExecutor.getInstance().submit(this::initialize);
    }

    @Override
    public List<Suggestion> contextVariablesOf(String componentFullyQualifiedName) {
        return completionTokensOf(componentFullyQualifiedName, StringUtils.EMPTY).stream()
                .filter(suggestion -> suggestion.getType() == SuggestionType.VARIABLE)
                .collect(toList());
    }

    @Override
    public List<Suggestion> completionTokensOf(String componentFullyQualifiedName, String token) {
        Optional<List<Suggestion>> componentSuggestions =
                componentTriesMap.getOrDefault(componentFullyQualifiedName, defaultComponentTrie).findByPrefix(token);
        Optional<List<Suggestion>> customFunctionsSuggestions =
                customFunctionsTrie.findByPrefix(token);
        List<Suggestion> results = new ArrayList<>();
        componentSuggestions.ifPresent(results::addAll);
        customFunctionsSuggestions.ifPresent(results::addAll);
        return results;
    }

    @Override
    public void onComponentListUpdate(Module module) {
        updateComponents(module);
    }

    private void updateComponents(Module module) {
        // Add suggestions from
        PluginExecutor.getInstance().submit(() -> internalUpdateComponents(module));
    }

    private void internalUpdateComponents(Module module) {
        Collection<ComponentsPackage> componentsPackages = ComponentService.getInstance(module).getModulesDescriptors();
        componentsPackages.forEach(componentsPackage -> componentsPackage.getModuleComponents()
                .forEach(descriptor -> descriptor.getPropertiesDescriptors().forEach(propertyDescriptor -> {
                    propertyDescriptor.getAutoCompleteContributorDefinition().ifPresent(definition -> {
                        String fullyQualifiedName = descriptor.getFullyQualifiedName();
                        boolean context = definition.isContext();
                        boolean message = definition.isMessage();
                        List<String> contributions = definition.getContributions();

                        final Trie trie = new Trie();
                        if (message) {
                            registerDefaultSuggestionContribution(trie, "message");
                        }
                        if (context) {
                            registerDefaultSuggestionContribution(trie, "context");
                        }
                        contributions.forEach(customSuggestion ->
                                SuggestionDefinitionMatcher.of(customSuggestion).ifPresent(parsed ->
                                        trie.insert(parsed.getMiddle(), parsed.getRight(), parsed.getLeft())));

                        componentTriesMap.put(fullyQualifiedName, trie);
                    });
                })));

        Collection<AutoCompleteContributorDefinition> autoCompleteDefinitions =
                ComponentService.getInstance(module).getAutoCompleteContributorDefinition();
        customFunctionsTrie = new Trie();
        autoCompleteDefinitions.forEach(definition -> definition.getContributions().forEach(contribution -> {
            SuggestionDefinitionMatcher.of(contribution).ifPresent(parsed ->
                    customFunctionsTrie.insert(parsed.getMiddle(), parsed.getRight(), parsed.getLeft()));
        }));

        onCompletionEvent.onCompletionsUpdated();

    }

    private void registerDefaultSuggestionContribution(Trie trie, String suggestionContributor) {
        String[] tokens = SuggestionsBundle.message(suggestionContributor).split(",");
        Arrays.stream(tokens).forEach(suggestionTokenDefinition ->
                SuggestionDefinitionMatcher.of(suggestionTokenDefinition).ifPresent(parsed ->
                        trie.insert(parsed.getMiddle(), parsed.getRight(), parsed.getLeft())));
    }

    private void initialize() {
        PluginExecutor.getInstance().submit(() -> {
            registerDefaultSuggestionContribution(defaultComponentTrie, "message");
            registerDefaultSuggestionContribution(defaultComponentTrie, "context");
            internalUpdateComponents(module);
        });
    }
}