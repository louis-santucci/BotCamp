package com.botcamp.gmail_gateway_api.service;

import com.botcamp.gmail_gateway_api.mailing.Email;
import com.botcamp.gmail_gateway_api.mailing.EmailHandlingException;

import java.io.IOException;
import java.util.List;

public interface GmailService {
    List<Email> getEmails(String beginDate, String endDate, String sender, String subject) throws IOException, InterruptedException, EmailHandlingException;
}
