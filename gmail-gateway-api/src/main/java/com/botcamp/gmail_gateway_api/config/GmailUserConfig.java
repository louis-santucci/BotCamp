package com.botcamp.gmail_gateway_api.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gmail.api.user")
@Data
@NoArgsConstructor
public class GmailUserConfig {

    private String email;
}
