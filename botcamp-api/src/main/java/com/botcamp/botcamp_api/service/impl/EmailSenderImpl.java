package com.botcamp.botcamp_api.service.impl;

import com.botcamp.botcamp_api.config.properties.BotcampEmailProperties;
import com.botcamp.botcamp_api.service.EmailSender;
import com.botcamp.common.exception.EmailParsingException;
import com.botcamp.common.utils.EmailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
@Slf4j
public class EmailSenderImpl implements EmailSender {

    private final Session mailSession;
    private final BotcampEmailProperties botcampEmailProperties;

    public EmailSenderImpl(Session session,
                           BotcampEmailProperties botcampEmailProperties) {
        this.mailSession = session;
        this.botcampEmailProperties = botcampEmailProperties;
    }

    @Override
    public void sendEmail(List<String> to, String subject, String body) throws UnsupportedEncodingException, MessagingException, EmailParsingException {
        MimeMessage message = EmailUtils.buildEmail(mailSession, botcampEmailProperties.getUsername(), to, subject, body);
        Transport.send(message);
        log.info("Email sent successfully to {}", to);
    }


}
