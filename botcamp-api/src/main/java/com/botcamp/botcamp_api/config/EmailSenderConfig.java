package com.botcamp.botcamp_api.config;

import com.botcamp.botcamp_api.config.properties.BotcampEmailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Configuration
@Import(BotcampEmailProperties.class)
public class EmailSenderConfig {

    private static final String MAIL_SMTP_HOST = "mail.smtp.host";
    private static final String MAIL_SMTP_PORT = "mail.smtp.port";
    private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    private static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
    private static final String MAIL_SMTP_SSL_PROTOCOLS = "mail.smtp.ssl.protocols";

    @Bean
    Properties botcampEmailProperties(BotcampEmailProperties botcampEmailProperties) {
        System.out.println("TLSEmail Start");
        Properties smtpProperties = new Properties();
        smtpProperties.put(MAIL_SMTP_HOST, botcampEmailProperties.getHost()); //SMTP Host
        smtpProperties.put(MAIL_SMTP_PORT, botcampEmailProperties.getPort()); //TLS Port
        smtpProperties.put(MAIL_SMTP_AUTH, botcampEmailProperties.isAuthEnabled()); //enable authentication
        smtpProperties.put(MAIL_SMTP_STARTTLS_ENABLE, botcampEmailProperties.isStartTlsEnabled()); //enable STARTTLS
        smtpProperties.put(MAIL_SMTP_SSL_PROTOCOLS, botcampEmailProperties.getSslProtocols());

        return smtpProperties;
    }

    @Bean
    Session session(Properties emailProperties,
                    Authenticator authenticator) {
        return Session.getInstance(emailProperties, authenticator);
    }

    @Bean
    Authenticator authenticator(BotcampEmailProperties properties) {
        return new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(properties.getUsername(), properties.getPassword());
            }
        };
    }
}
