package com.botcamp.gmail_gateway_api.controller.impl;

import com.botcamp.gmail_gateway_api.controller.MailController;
import com.botcamp.gmail_gateway_api.mailing.Email;
import com.botcamp.gmail_gateway_api.service.GmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

import static com.botcamp.gmail_gateway_api.controller.ControllerEndpoint.GET_LIST;

@RestController
@RequestMapping(path = "/api/v1/mail")
public class MailControllerImpl implements MailController {

    private static final String BEGIN_DATE_QUERY_PARAM = "beginDate";
    private static final String END_DATE_QUERY_PARAM = "endDate";
    private static final String TOPIC_QUERY_PARAM = "sender";
    private static final String SUBJECT_QUERY_PARAM = "subject";

    private GmailService gmailService;

    public MailControllerImpl(GmailService service) {
        this.gmailService = service;
    }


    @Override
    @GetMapping(GET_LIST)
    public ResponseEntity<?> getEmails(@RequestParam(name = BEGIN_DATE_QUERY_PARAM, required = false) String beginDate,
                                                @RequestParam(name = END_DATE_QUERY_PARAM, required = false) String endDate,
                                                @RequestParam(name = TOPIC_QUERY_PARAM, required = false) String sender,
                                                @RequestParam(name = SUBJECT_QUERY_PARAM, required = false) String subject) {
        try {
            List<Email> results = this.gmailService.getEmails(beginDate, endDate, sender, subject);
            return new ResponseEntity<>(results, HttpStatus.OK);
        } catch (IOException | InterruptedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
