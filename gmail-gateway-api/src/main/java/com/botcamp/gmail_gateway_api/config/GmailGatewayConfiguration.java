package com.botcamp.gmail_gateway_api.config;

import com.botcamp.common.config.DataSourceConfig;
import com.botcamp.common.config.PropertySourcesPlaceholderConfig;
import com.botcamp.gmail_gateway_api.config.properties.GmailUserConfigProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties
@Import({
        GmailUserConfigProperties.class,
        GmailAPICallerConfig.class,
        DataSourceConfig.class,
        GmailAPIOAuthConfiguration.class,
        WebSecurityConfig.class,
        PropertySourcesPlaceholderConfig.class
})
public class GmailGatewayConfiguration {
}
