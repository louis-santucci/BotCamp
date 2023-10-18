package com.botcamp.botcamp.configuration;

import com.google.api.services.gmail.Gmail;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;

@org.springframework.boot.test.context.TestConfiguration
@EnableScheduling
public class TestConfiguration {
    @Bean
    Gmail gmailTest() {
        return null;
    }
}
