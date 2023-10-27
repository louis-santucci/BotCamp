package com.botcamp.gmail_gateway_api.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gmail.api.caller")
@Data
@NoArgsConstructor
public class GmailAPICallerConfig {
    private int costLimit;
    private Long maxResults;
    private String resetCron;
    private Long costFrequency;
    private boolean cronEnabled;
}
