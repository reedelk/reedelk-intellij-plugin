package com.reedelk.plugin.service.project;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.project.Project;
import com.reedelk.plugin.service.project.impl.runconfiguration.PreferredRunConfigurationState;
import org.jetbrains.annotations.NotNull;

public interface PreferredRunConfigurationService extends PersistentStateComponent<PreferredRunConfigurationState> {

    static PreferredRunConfigurationService getInstance(@NotNull Project project) {
        return project.getComponent(PreferredRunConfigurationService.class);
    }

    void setLastModuleRunConfiguration(String name);

    void setLastRuntimeRunConfiguration(String name);
}
