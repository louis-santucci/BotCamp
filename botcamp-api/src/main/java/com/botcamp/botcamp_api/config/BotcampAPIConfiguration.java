package com.botcamp.botcamp_api.config;

import com.botcamp.config.properties.DataSourceConfigProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableConfigurationProperties
@Import({
        HttpConfig.class,
        DataSourceConfigProperties.class
})
public class BotcampAPIConfiguration {
}
