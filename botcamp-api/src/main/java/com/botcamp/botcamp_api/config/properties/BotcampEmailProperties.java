package com.botcamp.botcamp_api.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "botcamp.smtp")
public class BotcampEmailProperties {
    private String host;
    private Integer port;
    private String username;
    private String password;
    private boolean authEnabled;
    private boolean startTlsEnabled;
    private String sslProtocols;
}
