package com.botcamp.common.config.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "security.jwt")
public class JwtConfigProperties {
    private String secret;
    private Long tokenValidity;
}
