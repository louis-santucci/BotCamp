package com.botcamp.gmail_gateway_api.controller;

import org.springframework.http.ResponseEntity;

public interface MailController {
    ResponseEntity<?> getEmails(String beginDate, String endDate, String sender, String subject);
}
