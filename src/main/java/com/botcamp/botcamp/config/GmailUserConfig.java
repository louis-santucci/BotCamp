package com.botcamp.botcamp.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@AllArgsConstructor
@ConfigurationProperties(prefix = "gmail-user")
public class GmailUserConfig {

    private String email;
}
