package com.botcamp.botcamp_api.config;

import com.botcamp.common.config.DataSourceConfig;
import com.botcamp.common.config.PropertySourcesPlaceholderConfig;
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
        EmailSenderConfig.class
})
public class BotcampAPIConfiguration {
}
