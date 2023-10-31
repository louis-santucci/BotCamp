package com.botcamp.gmail_gateway_api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "db")
public class DataSourceConfigProperties {
    private int port;
    private String hostname;
    private String username;
    private String password;
    private String dbName;
    private String dbUrl;
    private String dbType;
}
