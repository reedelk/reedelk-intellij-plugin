package com.reedelk.plugin.action.importopenapi.handler;

import com.reedelk.plugin.action.importopenapi.ImporterOpenAPIContext;
import com.reedelk.plugin.template.Template;
import com.reedelk.runtime.api.commons.StringUtils;
import com.reedelk.runtime.commons.FileExtension;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;

import java.util.Properties;
import java.util.UUID;

public class POSTHandler implements Handler {

    @Override
    public boolean isApplicable(PathItem pathItem) {
        return pathItem.getPost() != null;
    }

    @Override
    public void accept(ImporterOpenAPIContext context, String pathEntry, PathItem pathItem) {
        String description = getOrDefault(pathItem.getDescription(), StringUtils.EMPTY);

        Operation postOperation = pathItem.getPost();
        String operationId = postOperation.getOperationId();
        String summary = getOrDefault(postOperation.getSummary(), operationId + " Flow");
        String operationDescription = "Path: " + pathEntry;

        Properties properties = new Properties();
        properties.put("id", UUID.randomUUID().toString());
        properties.put("title", summary);
        properties.put("description", description);
        properties.put("operationDescription", operationDescription);
        properties.put("configId", "");
        properties.put("operationPath", pathEntry);
        properties.put("operationMethod", "POST");

        String fileName = operationId + "." + FileExtension.FLOW.value();
        context.createTemplate(Template.OpenAPI.FLOW_WITH_REST_LISTENER, fileName, properties);
    }

    private String getOrDefault(String value, String defaultValue) {
        return StringUtils.isBlank(value) ? defaultValue : value;
    }
}
