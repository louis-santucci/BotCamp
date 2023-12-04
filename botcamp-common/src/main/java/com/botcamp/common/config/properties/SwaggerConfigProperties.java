package com.botcamp.common.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "swagger")
@Data
public class SwaggerConfigProperties {
    private String title;
    private String description;
    private String version;
    private SwaggerContactProperties contact;
}
