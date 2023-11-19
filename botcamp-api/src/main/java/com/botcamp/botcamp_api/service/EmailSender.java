package com.botcamp.botcamp_api.service;

import com.botcamp.common.exception.EmailParsingException;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface EmailSender {
    void sendEmail(List<String> to, String subject, String body) throws UnsupportedEncodingException, MessagingException, EmailParsingException;
}
