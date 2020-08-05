package com.reedelk.plugin.action.openapi.importer;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.reedelk.openapi.v3.model.SchemaObject;
import com.reedelk.plugin.commons.PluginModuleUtils;
import com.reedelk.plugin.template.ConfigProperties;
import com.reedelk.plugin.template.Template;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.nio.file.Paths;
import java.util.*;

public class OpenApiImporterContext {

    private final String openApiFilePath;
    private Map<String,String> schemaIdAndPath = new HashMap<>();
    private final Project project;
    private final String configId = UUID.randomUUID().toString();

    public OpenApiImporterContext(@NotNull Project project, String openApiFilePath) {
        this.project = project;
        this.openApiFilePath = openApiFilePath;
    }

    public String getOpenApiFilePath() {
        return openApiFilePath;
    }

    public SchemaFormat getSchemaFormat() {
        return SchemaFormat.formatOf(openApiFilePath);
    }

    public String getConfigId() {
        return configId;
    }

    // TODO: Add target directory.
    public Optional<String> createSchema(String schemaId, SchemaObject schemaObject, SchemaFormat schemaFormat) {
        if (schemaObject.getSchema() != null) {
            Map<String, Object> schemaData = schemaObject.getSchema().getSchemaData();
            Properties properties = new Properties();

            if (SchemaFormat.JSON.equals(schemaFormat)) {
                properties.put("schema", new JSONObject(schemaData).toString(2));
            }

            if (SchemaFormat.YAML.equals(schemaFormat)) {
                properties.put("schema", new Yaml().dump(schemaData));
            }

            Module[] modules = ModuleManager.getInstance(project).getModules();
            // TODO: This is wrong. We should get the current module or the module should
            //      be in a list when importing the project.
            Module module = modules[0];

            String finalFileName = schemaId + "." + schemaFormat.getExtension();
            Optional<String> flowsDirectory = PluginModuleUtils.getAssetsDirectory(module);
            flowsDirectory.ifPresent(directory -> WriteCommandAction.runWriteCommandAction(project, () -> {
                VirtualFile flowsFolderVf = VfsUtil.findFile(Paths.get(directory), true);
                Template.OpenAPI.SCHEMA.create(project, properties, flowsFolderVf, finalFileName)
                        .ifPresent(virtualFile -> FileEditorManager.getInstance(project)
                                .openFile(virtualFile, false));
            }));

            return  Optional.of("assets/" + finalFileName);
        }
        return Optional.empty();
    }



    public String createSchema(String fileName, Properties properties) {
        Module[] modules = ModuleManager.getInstance(project).getModules();
        // TODO: This is wrong. We should get the current module or the module should
        //      be in a list when importing the project.
        Module module = modules[0];

        Optional<String> assetsDirectory = PluginModuleUtils.getAssetsDirectory(module);
        assetsDirectory.ifPresent(directory -> WriteCommandAction.runWriteCommandAction(project, () -> {
            VirtualFile assetsDirectoryVf = VfsUtil.findFile(Paths.get(directory), true);
            Template.OpenAPI.SCHEMA.create(project, properties, assetsDirectoryVf, fileName);
        }));

        return  "assets/" + fileName;
    }

    public void createFlow(String fileName, Properties properties) {

        // TODO: Module 0 is wrong. The module should be selectable from the Dialog.
        Module[] modules = ModuleManager.getInstance(project).getModules();
        Module module = modules[0];

        Optional<String> flowsDirectory = PluginModuleUtils.getFlowsDirectory(module);
        flowsDirectory.ifPresent(directory -> WriteCommandAction.runWriteCommandAction(project, () -> {
            VirtualFile flowsFolderVf = VfsUtil.findFile(Paths.get(directory), true);
            Template.OpenAPI.FLOW_WITH_REST_LISTENER.create(project, properties, flowsFolderVf, fileName)
                    .ifPresent(virtualFile -> FileEditorManager.getInstance(project)
                            .openFile(virtualFile, true));
        }));
    }

    public void createConfig(String configFileName, String configOpenApi) {
        String title = "Open API Config";

        // TODO: Module 0 is wrong. The module should be selectable from the Dialog.
        Module[] modules = ModuleManager.getInstance(project).getModules();
        Module module = modules[0];

        Optional<String> configsFolder = PluginModuleUtils.getConfigsDirectory(module);
        configsFolder.ifPresent(configsFolder1 -> WriteCommandAction.runWriteCommandAction(project, () -> {
            VirtualFile configsDirectoryVf = VfsUtil.findFile(Paths.get(configsFolder1), true);
            ConfigProperties configProperties = new ConfigProperties(configId, title);
            configProperties.put("openApiObject", configOpenApi);
            Template.OpenAPI.FLOW_CONFIG.create(project, configProperties, configsDirectoryVf, configFileName);
        }));
    }

    public void register(String schemaId, String schemaAssetPath) {
        schemaIdAndPath.put(schemaId, schemaAssetPath);
    }

    public Optional<String> assetFrom(String $ref) {
        for (Map.Entry<String,String> schemaIdAndPath : schemaIdAndPath.entrySet()) {
            String schemaId = schemaIdAndPath.getKey();
            if ($ref.endsWith(schemaId)) {
                return Optional.of(schemaIdAndPath.getValue());
            }
        }
        return Optional.empty();
    }
}
