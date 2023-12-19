package com.botcamp.botcamp_api.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

@ConfigurationProperties(prefix = "http-config")
@Data
public class HttpConfigProperties {
    private Long connectionTimeout;
    private int socketTimeout;
    private TimeUnit connectionTimeoutUnit;
    private TimeUnit socketTimeoutUnit;
    private boolean isLoggingEnabled;
}
