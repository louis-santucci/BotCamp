package com.botcamp.gmail_gateway_api.service;

import com.botcamp.common.exception.UnknownUserException;
import com.botcamp.gmail_gateway_api.config.GatewayUser;
import com.botcamp.common.exception.EmailHandlingException;
import com.botcamp.common.mail.EmailResults;

import java.io.IOException;

public interface GmailService {
    EmailResults getEmails(GatewayUser gatewayUser, String beginDate, String endDate, String sender, String subject) throws IOException, InterruptedException, EmailHandlingException, UnknownUserException;
}
