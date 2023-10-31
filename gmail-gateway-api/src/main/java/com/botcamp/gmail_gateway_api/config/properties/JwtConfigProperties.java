package com.botcamp.gmail_gateway_api.config.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtConfigProperties {
    private String secret;
    private Long tokenValidity;
}
