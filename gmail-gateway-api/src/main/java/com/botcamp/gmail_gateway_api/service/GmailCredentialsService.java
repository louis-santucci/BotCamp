package com.botcamp.gmail_gateway_api.service;

import com.botcamp.common.exception.EmailParsingException;
import com.botcamp.common.gateway_credentials.GmailCredential;

import java.io.IOException;
import java.util.Map;

public interface GmailCredentialsService {
    GmailCredential createGmailCredential(String gmailEmail) throws EmailParsingException, IOException;
    Map<String, GmailCredential> getGmailCredentials();
    void initGmailCredentialsMap() throws IOException, EmailParsingException;
    void clearGmailCredentials(boolean deleteFile) throws IOException;

}
