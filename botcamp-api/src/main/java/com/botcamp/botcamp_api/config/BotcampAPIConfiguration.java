package com.botcamp.botcamp_api.config;

import com.botcamp.botcamp_api.config.http.HttpConfig;
import com.botcamp.common.config.DataSourceConfig;
import com.botcamp.common.config.PropertySourcesPlaceholderConfig;
import com.botcamp.common.config.SwaggerConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableConfigurationProperties
@Import({
        HttpConfig.class,
        DataSourceConfig.class,
        PropertySourcesPlaceholderConfig.class,
        EmailSenderConfig.class,
        SwaggerConfig.class
})
public class BotcampAPIConfiguration {
}
