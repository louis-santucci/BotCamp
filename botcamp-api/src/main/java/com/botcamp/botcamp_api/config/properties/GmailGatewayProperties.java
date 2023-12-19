package com.botcamp.botcamp_api.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Import;

@ConfigurationProperties(prefix = "gmail-gateway")
@Data
@Import(GmailGatewayCredentialsProperties.class)
public class GmailGatewayProperties {
    private String protocol;
    private String url;
    private int port;
    private GmailGatewayCredentialsProperties credentials;
}
