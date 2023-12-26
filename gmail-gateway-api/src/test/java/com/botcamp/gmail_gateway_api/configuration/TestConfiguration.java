package com.botcamp.gmail_gateway_api.configuration;

import com.botcamp.gmail_gateway_api.controller.MailController;
import com.botcamp.gmail_gateway_api.service.GmailCredentialFactory;
import com.botcamp.gmail_gateway_api.service.GmailCredentialsService;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.services.gmail.Gmail;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.annotation.EnableScheduling;

@org.springframework.boot.test.context.TestConfiguration
@EnableScheduling
public class TestConfiguration {

    @MockBean
    private Gmail gmail;

    @MockBean
    private MailController mailController;

    @MockBean
    private GoogleClientSecrets googleClientSecrets;

    @MockBean
    private GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow;

    @MockBean
    private GmailCredentialsService gmailCredentialsService;

    @MockBean
    private GmailCredentialFactory gmailCredentialFactory;
}
