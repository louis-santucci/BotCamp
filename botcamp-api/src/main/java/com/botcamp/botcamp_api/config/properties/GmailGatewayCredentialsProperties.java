package com.botcamp.botcamp_api.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gmail-gateway.credentials")
@Data
public class GmailGatewayCredentialsProperties {
    private String username;
    private String password;
}
