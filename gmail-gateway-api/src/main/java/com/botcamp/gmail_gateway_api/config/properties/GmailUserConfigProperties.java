package com.botcamp.gmail_gateway_api.config.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gmail.api.user")
@Data
@NoArgsConstructor
public class GmailUserConfigProperties {

    private String email;
}
