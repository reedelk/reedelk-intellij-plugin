package com.reedelk.plugin.commons;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowId;
import com.intellij.openapi.wm.ToolWindowManager;
import com.reedelk.plugin.editor.properties.PropertiesPanelToolWindowFactory;
import com.reedelk.runtime.api.commons.StringUtils;

import javax.swing.*;
import java.util.Optional;

import static java.util.Arrays.stream;

public class ToolWindowUtils {

    private ToolWindowUtils() {
    }

    public static void switchToolWindowAndNotifyWithMessage(Project project, String message, String runConfigName) {
        SwingUtilities.invokeLater(() -> {
            NotificationUtils.notifyInfo(ToolWindowId.RUN, message, project);
            get(project, ToolWindowId.RUN).ifPresent(window ->
                    stream(window.getContentManager().getContents()).forEach(content -> {
                        if (StringUtils.isNotBlank(runConfigName)) {
                            if (runConfigName.equals(content.getDisplayName())) {
                                window.getContentManager().setSelectedContent(content, false);
                                window.show(() -> {
                                });
                            }
                        }
                    }));
        });
    }

    public static void show(Project project) {
        ToolWindow toolWindow = get(project);
        if (!toolWindow.isVisible()) {
            toolWindow.show(() -> {
            });
        }
    }

    public static Optional<ToolWindow> get(Project project, String id) {
        return Optional.ofNullable(ToolWindowManager.getInstance(project).getToolWindow(id));
    }

    public static ToolWindow get(Project project) {
        return ToolWindowManager
                .getInstance(project)
                .getToolWindow(PropertiesPanelToolWindowFactory.ID);
    }
}
