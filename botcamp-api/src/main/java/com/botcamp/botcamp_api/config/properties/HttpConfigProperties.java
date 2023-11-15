package com.botcamp.botcamp_api.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "http-config")
@Data
public class HttpConfigProperties {
    private Long requestTimeout;
}
