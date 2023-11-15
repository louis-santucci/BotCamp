package com.botcamp.common.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Import;

@Data
@ConfigurationProperties(prefix = "security")
@Import(JwtConfigProperties.class)
public class SecurityConfigProperties {
    private boolean enabled;
    private JwtConfigProperties jwt;
}
