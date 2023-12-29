package com.botcamp.gmail_gateway_api.service;

import com.google.api.client.auth.oauth2.Credential;

import java.io.IOException;

public interface GmailCredentialFactory {
    Credential buildCredential(String gmailEmail) throws IOException;
}
