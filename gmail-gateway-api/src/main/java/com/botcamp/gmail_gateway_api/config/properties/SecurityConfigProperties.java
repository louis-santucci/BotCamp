package com.botcamp.gmail_gateway_api.config.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Import;

@Data
@ConfigurationProperties(prefix = "security")
@Import(JwtConfigProperties.class)
public class SecurityConfigProperties {
    private boolean enabled;
    private JwtConfigProperties jwt;
}
