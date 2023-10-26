package com.botcamp.botcamp.configuration;

import com.botcamp.botcamp.config.GmailUserConfig;
import com.botcamp.botcamp.service.mail.MessageHandler;
import com.botcamp.botcamp.service.mail.impl.GmailMessageHandlerImpl;
import com.google.api.services.gmail.Gmail;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@TestConfiguration
@EnableScheduling
public class BotCampTestConfiguration {
    @Bean
    Gmail gmailTest() {
        return null;
    }

    @Bean
    MessageHandler messageHandler() {
        return new GmailMessageHandlerImpl();
    }

}
