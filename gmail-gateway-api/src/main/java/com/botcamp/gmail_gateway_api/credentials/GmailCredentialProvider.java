package com.botcamp.gmail_gateway_api.credentials;

import com.botcamp.common.exception.EmailParsingException;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.gmail.Gmail;

import java.io.IOException;

public interface GmailCredentialProvider {
    Credential authorize(String gmailEmail) throws IOException;
}
