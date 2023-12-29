package com.botcamp.botcamp_api.service;

import com.botcamp.common.jwt.JwtToken;
import com.botcamp.common.mail.EmailResults;

import java.io.IOException;

public interface GmailGatewayClient {
    JwtToken requestNewToken();
    EmailResults getEmails(String beginDate, String endDate, String sender, String subject, boolean compress) throws IOException;
}
