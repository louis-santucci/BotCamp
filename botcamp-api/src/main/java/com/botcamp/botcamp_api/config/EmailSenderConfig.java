package com.botcamp.botcamp_api.config;

import com.botcamp.botcamp_api.config.properties.BotcampEmailProperties;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(BotcampEmailProperties.class)
public class EmailSenderConfig {


    @Bean
    Mailer mailer(BotcampEmailProperties properties) {
        return MailerBuilder
                .withSMTPServerHost(properties.getHost())
                .withSMTPServerPort(properties.getPort())
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .withSMTPServerUsername(properties.getUsername())
                .withSMTPServerPassword(properties.getPassword())
                .buildMailer();
    }
}
