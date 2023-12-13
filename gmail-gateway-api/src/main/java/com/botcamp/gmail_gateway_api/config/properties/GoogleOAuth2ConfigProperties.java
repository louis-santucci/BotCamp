package com.botcamp.gmail_gateway_api.config.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "gmail.config")
public class GoogleOAuth2ConfigProperties {
    private String tokenDirectoryName;
    private String credentialsFilePath;
    private String storedCredentialsFileName;
    private List<String> scopes;
    private int callbackServerPort;
}
