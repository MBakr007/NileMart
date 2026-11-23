package org.route.nilemartspringboot.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/**
 * General information shown at the top of the OpenAPI document and Swagger UI.
 */
@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Nile Mart API",
        version = "v1",
        description = "REST API for the Nile Mart store"))
public class OpenApiConfig {
}
