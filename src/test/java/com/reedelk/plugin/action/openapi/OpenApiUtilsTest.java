package com.reedelk.plugin.action.openapi;

import com.reedelk.openapi.commons.NavigationPath;
import com.reedelk.openapi.v3.model.InfoObject;
import com.reedelk.openapi.v3.model.OpenApiObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.reedelk.openapi.commons.NavigationPath.SegmentKey;
import static com.reedelk.openapi.commons.NavigationPath.create;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class OpenApiUtilsTest {

    @Mock
    private OpenApiImporterContext context;

    @BeforeEach
    void setUp() {
        lenient().doReturn(OpenApiSchemaFormat.JSON).when(context).getSchemaFormat();
    }

    @Test
    void shouldReturnCorrectConfigTitle() {
        // Given
        InfoObject infoObject = new InfoObject();
        infoObject.setTitle("Test API");
        OpenApiObject openApiObject = new OpenApiObject();
        openApiObject.setInfo(infoObject);

        // When
        String actual = OpenApiUtils.restListenerConfigTitleFrom(openApiObject);

        // Then
        String expected = "Test API REST Listener";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldReturnDefaultConfigTitle() {
        // Given
        OpenApiObject openApiObject = new OpenApiObject();

        // When
        String actual = OpenApiUtils.restListenerConfigTitleFrom(openApiObject);

        // Then
        String expected = "My Api REST Listener";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldReturnCorrectConfigFileName() {
        // Given
        InfoObject infoObject = new InfoObject();
        infoObject.setTitle("Test API");
        OpenApiObject openApiObject = new OpenApiObject();
        openApiObject.setInfo(infoObject);

        // When
        String actual = OpenApiUtils.restListenerConfigFileNameFrom(openApiObject);

        // Then
        String expected = "TestAPIRESTListener.fconfig";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldReturnDefaultConfigFileName() {
        // Given
        OpenApiObject openApiObject = new OpenApiObject();

        // When
        String actual = OpenApiUtils.restListenerConfigFileNameFrom(openApiObject);

        // Then
        String expected = "MyApiRESTListener.fconfig";
        assertThat(actual).isEqualTo(expected);
    }

    // Schema Response
    @Test
    void shouldReturnCorrectSchemaResponseFileNameWhenOperationIdPresent() {
        // Given
        NavigationPath navigationPath = create()
                .with(SegmentKey.OPERATION_ID, "getPetById")
                .with(SegmentKey.METHOD, "GET")
                .with(SegmentKey.PATH, "/order/{id}")
                .with(SegmentKey.RESPONSES)
                .with(SegmentKey.STATUS_CODE, "200")
                .with(SegmentKey.CONTENT)
                .with(SegmentKey.CONTENT_TYPE, "application/json");

        // When
        String actual = OpenApiUtils.schemaFileNameFrom(navigationPath, context);

        // Then
        assertThat(actual).isEqualTo("getPetById_response_200_application_json.schema.json");
    }

    @Test
    void shouldReturnCorrectSchemaResponseFileNameWhenOperationIdNotPresent() {
        // Given
        NavigationPath navigationPath = create()
                .with(SegmentKey.METHOD, "GET")
                .with(SegmentKey.PATH, "/order/{id}")
                .with(SegmentKey.RESPONSES)
                .with(SegmentKey.STATUS_CODE, "200")
                .with(SegmentKey.CONTENT)
                .with(SegmentKey.CONTENT_TYPE, "application/json");

        // When
        String actual = OpenApiUtils.schemaFileNameFrom(navigationPath, context);

        // Then
        assertThat(actual).isEqualTo("getOrderId_response_200_application_json.schema.json");
    }

    @Test
    void shouldReturnCorrectSchemaResponseFileNameWhenOperationIdNotPresentAndRootPath() {
        // Given
        NavigationPath navigationPath = create()
                .with(SegmentKey.METHOD, "GET")
                .with(SegmentKey.PATH, "/")
                .with(SegmentKey.RESPONSES)
                .with(SegmentKey.STATUS_CODE, "200")
                .with(SegmentKey.CONTENT)
                .with(SegmentKey.CONTENT_TYPE, "application/json");

        // When
        String actual = OpenApiUtils.schemaFileNameFrom(navigationPath, context);

        // Then
        assertThat(actual).isEqualTo("get_response_200_application_json.schema.json");
    }

    // Schema Request Body

    @Test
    void shouldReturnCorrectSchemaRequestBodyFileNameWhenOperationIdPresent() {
        // Given
        NavigationPath navigationPath = create()
                .with(SegmentKey.OPERATION_ID, "getPetById")
                .with(SegmentKey.METHOD, "GET")
                .with(SegmentKey.PATH, "/order/{id}")
                .with(SegmentKey.REQUEST_BODY)
                .with(SegmentKey.CONTENT)
                .with(SegmentKey.CONTENT_TYPE, "application/x-www-form-urlencoded");

        // When
        String actual = OpenApiUtils.schemaFileNameFrom(navigationPath, context);

        // Then
        assertThat(actual).isEqualTo("getPetById_requestBody_application_x-www-form-urlencoded.schema.json");
    }

    @Test
    void shouldReturnCorrectSchemaRequestBodyFileNameWhenOperationIdNotPresent() {
        // Given
        NavigationPath navigationPath = create()
                .with(SegmentKey.METHOD, "GET")
                .with(SegmentKey.PATH, "/order/{id}")
                .with(SegmentKey.REQUEST_BODY)
                .with(SegmentKey.CONTENT)
                .with(SegmentKey.CONTENT_TYPE, "application/x-www-form-urlencoded");

        // When
        String actual = OpenApiUtils.schemaFileNameFrom(navigationPath, context);

        // Then
        assertThat(actual).isEqualTo("getOrderId_requestBody_application_x-www-form-urlencoded.schema.json");
    }

    @Test
    void shouldReturnCorrectSchemaRequestBodyFileNameWhenOperationIdNotPresentAndRootPath() {
        // Given
        NavigationPath navigationPath = create()
                .with(SegmentKey.METHOD, "GET")
                .with(SegmentKey.PATH, "/")
                .with(SegmentKey.REQUEST_BODY)
                .with(SegmentKey.CONTENT)
                .with(SegmentKey.CONTENT_TYPE, "application/x-www-form-urlencoded");

        // When
        String actual = OpenApiUtils.schemaFileNameFrom(navigationPath, context);

        // Then
        assertThat(actual).isEqualTo("get_requestBody_application_x-www-form-urlencoded.schema.json");
    }

    // Example
    @Test
    void shouldReturnCorrectExampleFileNameWhenOperationIdPresent() {
        // Given
        NavigationPath navigationPath = create()
                .with(SegmentKey.OPERATION_ID, "getPetById")
                .with(SegmentKey.METHOD, "GET")
                .with(SegmentKey.PATH, "/order/{id}")
                .with(SegmentKey.RESPONSES)
                .with(SegmentKey.STATUS_CODE, "200")
                .with(SegmentKey.CONTENT)
                .with(SegmentKey.CONTENT_TYPE, "application/json");

        // When
        String actual = OpenApiUtils.exampleFileNameFrom(navigationPath, context);

        // Then
        assertThat(actual).isEqualTo("getPetById_response_200_application_json.example.json");
    }

    @Test
    void shouldReturnCorrectExampleFileNameWhenOperationIdNotPresent() {
        // Given
        NavigationPath navigationPath = create()
                .with(SegmentKey.METHOD, "GET")
                .with(SegmentKey.PATH, "/order/{id}")
                .with(SegmentKey.RESPONSES)
                .with(SegmentKey.STATUS_CODE, "200")
                .with(SegmentKey.CONTENT)
                .with(SegmentKey.CONTENT_TYPE, "application/json");

        // When
        String actual = OpenApiUtils.exampleFileNameFrom(navigationPath, context);

        // Then
        assertThat(actual).isEqualTo("getOrderId_response_200_application_json.example.json");
    }

    @Test
    void shouldReturnCorrectExampleFileNameWhenOperationIdNotPresentAndRootPath() {
        // Given
        NavigationPath navigationPath = create()
                .with(SegmentKey.METHOD, "GET")
                .with(SegmentKey.PATH, "/")
                .with(SegmentKey.RESPONSES)
                .with(SegmentKey.STATUS_CODE, "200")
                .with(SegmentKey.CONTENT)
                .with(SegmentKey.CONTENT_TYPE, "application/json");

        // When
        String actual = OpenApiUtils.exampleFileNameFrom(navigationPath, context);

        // Then
        assertThat(actual).isEqualTo("get_response_200_application_json.example.json");
    }
}
