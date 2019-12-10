package com.reedelk.plugin.runconfig.runtime;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class RuntimeRunConfigurationFactory extends ConfigurationFactory {

    private static final String FACTORY_NAME = "Runtime Run Configuration Factory";

    public RuntimeRunConfigurationFactory(@NotNull ConfigurationType type) {
        super(type);
    }

    @NotNull
    @Override
    public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new RuntimeRunConfiguration(project, this, "Runtime Run Configuration");
    }

    @NotNull
    @Override
    public String getName() {
        return FACTORY_NAME;
    }
}