package com.botcamp.common.config;

import com.botcamp.common.config.properties.SwaggerConfigProperties;
import com.botcamp.common.config.properties.SwaggerContactProperties;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@Import({
        SwaggerConfigProperties.class,
        SwaggerContactProperties.class
})
public class SwaggerConfig {
    public static final String BEARER_AUTHENTICATION = "Bearer Authentication";

    private static final String BEARER = "bearer";
    private static final String JWT = "JWT";

    @Bean
    public OpenAPI openAPI(SwaggerConfigProperties swaggerConfigProperties) {
        Info info = new Info();

        info.setContact(buildContact(swaggerConfigProperties.getContact()));
        info.setDescription(swaggerConfigProperties.getDescription());
        info.setTitle(swaggerConfigProperties.getTitle());

        return new OpenAPI()
                .components(new Components().addSecuritySchemes(BEARER_AUTHENTICATION, createApiKeyScheme()))
                .info(info);
    }

    private SecurityScheme createApiKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat(JWT)
                .scheme(BEARER);
    }

    private Contact buildContact(SwaggerContactProperties contactProperties) {
        Contact contact = new Contact();

        contact.setName(contactProperties.getName());
        contact.setEmail(contactProperties.getEmail());
        contact.setUrl(contactProperties.getUrl());

        return contact;
    }
}
