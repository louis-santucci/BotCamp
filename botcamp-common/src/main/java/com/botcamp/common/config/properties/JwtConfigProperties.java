package com.botcamp.common.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.temporal.ChronoUnit;

@Data
@ConfigurationProperties(prefix = "security.jwt")
public class JwtConfigProperties {
    private String secret;
    private Long tokenValidity;
    private ChronoUnit unit;
}
