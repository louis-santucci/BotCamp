package com.botcamp.botcamp.controller;

import org.springframework.http.ResponseEntity;

public interface MailController {
    ResponseEntity<?> getEmails(String beginDate, String endDate, String sender, String subject);

    interface Endpoints {
        String GET_LIST = "/all";
    }
}
