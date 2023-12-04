package com.botcamp.common.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "swagger.contact")
public class SwaggerContactProperties {
    private String email;
    private String name;
    private String url;
}
