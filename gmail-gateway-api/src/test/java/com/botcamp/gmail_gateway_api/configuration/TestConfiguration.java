package com.botcamp.gmail_gateway_api.configuration;

import com.google.api.services.gmail.Gmail;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@org.springframework.boot.test.context.TestConfiguration
@EnableScheduling
public class TestConfiguration {
    @Bean
    Gmail gmailTest() {
        return null;
    }
}
