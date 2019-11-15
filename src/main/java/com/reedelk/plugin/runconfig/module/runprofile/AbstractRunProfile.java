package com.reedelk.plugin.runconfig.module.runprofile;

import com.intellij.execution.*;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.reedelk.plugin.commons.MavenUtils;
import com.reedelk.plugin.commons.NotificationUtils;
import com.reedelk.plugin.runconfig.runtime.RuntimeRunConfiguration;
import com.reedelk.plugin.service.project.ToolWindowService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.idea.maven.model.MavenId;
import org.jetbrains.idea.maven.project.MavenProject;

import java.io.File;
import java.util.Optional;

import static com.reedelk.plugin.commons.Messages.ModuleRun.*;
import static com.reedelk.runtime.api.commons.StringUtils.isBlank;
import static java.lang.String.format;

abstract class AbstractRunProfile implements RunProfileState {

    protected final Project project;
    protected final String moduleName;
    protected final String runtimeConfigName;

    String address;
    int port;

    AbstractRunProfile(final Project project, final String moduleName, String runtimeConfigName) {
        this.project = project;
        this.moduleName = moduleName;
        this.runtimeConfigName = runtimeConfigName;
    }

    @Nullable
    @Override
    public ExecutionResult execute(Executor executor, @NotNull ProgramRunner runner) throws ExecutionException {

        if (isBlank(moduleName)) {
            String actionName = executor.getActionName();
            String errorMessage = ERROR_GENERIC_MODULE_NOT_SELECTED.format(actionName);
            throw new ExecutionException(errorMessage);
        }

        Optional<MavenProject> optionalMavenProject = MavenUtils.getMavenProject(project, moduleName);
        if (!optionalMavenProject.isPresent()) {
            throw new ExecutionException(ERROR_MAVEN_PROJECT_NOT_FOUND.format(moduleName));
        }

        MavenProject mavenProject = optionalMavenProject.get();
        String moduleJarFilePath = getModuleJarFile(mavenProject);

        RunnerAndConfigurationSettings configSettings = RunManager.getInstance(project).findConfigurationByName(runtimeConfigName);
        if (configSettings == null) {
            throw new ExecutionException(ERROR_RUNTIME_CONFIG_NOT_FOUND.format(runtimeConfigName));
        }

        RuntimeRunConfiguration runtimeRunConfiguration = (RuntimeRunConfiguration) configSettings.getConfiguration();

        this.port = Integer.parseInt(runtimeRunConfiguration.getRuntimePort());
        this.address = runtimeRunConfiguration.getRuntimeBindAddress();

        return execute(mavenProject, moduleJarFilePath);
    }

    protected abstract ExecutionResult execute(@NotNull MavenProject mavenProject, @NotNull String moduleFile) throws ExecutionException;


    void switchToolWindowAndNotifyWithMessage(String message) {
        ToolWindowService toolWindowService = ServiceManager.getService(project, ToolWindowService.class);
        Optional<String> optionalToolWindowId = toolWindowService.get(runtimeConfigName);
        optionalToolWindowId.ifPresent(toolWindowId -> getToolWindowById(toolWindowId).show(null));
        optionalToolWindowId.ifPresent(toolWindowId -> NotificationUtils.notifyInfo(toolWindowId, message, project));
    }

    private ToolWindow getToolWindowById(String toolWindowId) {
        return ToolWindowManager
                .getInstance(project)
                .getToolWindow(toolWindowId);
    }

    private String getModuleJarFile(MavenProject mavenProject) {
        String targetDir = mavenProject.getBuildDirectory();
        MavenId mavenId = mavenProject.getMavenId();
        return format("file:%s" + File.separator + "%s-%s.jar",
                targetDir,
                mavenId.getArtifactId(),
                mavenId.getVersion());
    }
}
