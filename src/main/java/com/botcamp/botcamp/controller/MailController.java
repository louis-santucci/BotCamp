package com.botcamp.botcamp.controller;

import org.springframework.http.ResponseEntity;

public interface MailController {
    ResponseEntity<?> getEmails(String beginDate, String endDate, String sender, String subject);

    String GET_LIST = "/all";
}
