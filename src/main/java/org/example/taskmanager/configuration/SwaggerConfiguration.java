package org.example.taskmanager.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    public static final String BARER_SCHEME = "bearer";
    public static final String BARER_FORMAT = "JWT";
    public static final String VERSION = "0.1";
    public static final String BARER_AUTH = "Bearer Authentication";
    public static final String TITLE = "My REST API";
    public static final String DESCRIPTION = "Description of API.";

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat(BARER_FORMAT)
                .scheme(BARER_SCHEME);
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().addSecurityItem(new SecurityRequirement().addList(BARER_AUTH))
                .components(new Components().addSecuritySchemes
                        (BARER_AUTH, createAPIKeyScheme()))
                .info(new Info().title(TITLE)
                        .description(DESCRIPTION)
                        .version(VERSION)
                );
    }
}