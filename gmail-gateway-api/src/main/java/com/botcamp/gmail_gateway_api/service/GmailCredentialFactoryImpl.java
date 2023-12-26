package com.botcamp.gmail_gateway_api.service;

import com.botcamp.gmail_gateway_api.config.properties.GoogleOAuth2ConfigProperties;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.util.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
@Slf4j
public class GmailCredentialFactoryImpl implements GmailCredentialFactory {
    private final GoogleAuthorizationCodeFlow googleAuthCodeFlow;
    private final AuthorizationCodeInstalledApp.Browser browser;
    private final GoogleOAuth2ConfigProperties googleOAuth2ConfigProperties;


    public GmailCredentialFactoryImpl(GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow,
                                      GoogleOAuth2ConfigProperties googleOAuth2ConfigProperties,
                                      AuthorizationCodeInstalledApp.Browser browser) {
        this.googleAuthCodeFlow = googleAuthorizationCodeFlow;
        this.browser = browser;
        this.googleOAuth2ConfigProperties = googleOAuth2ConfigProperties;
    }


    @Override
    public Credential buildCredential(String gmailEmail) throws IOException {
        VerificationCodeReceiver receiver = new LocalServerReceiver
                .Builder()
                .setPort(googleOAuth2ConfigProperties.getCallbackServerPort())
                .build();

        Credential var3;
        try {
            Credential credential = this.googleAuthCodeFlow.loadCredential(gmailEmail);
            if (credential == null || credential.getRefreshToken() == null && credential.getExpiresInSeconds() != null && credential.getExpiresInSeconds() <= 60L) {
                String redirectUri = receiver.getRedirectUri();
                AuthorizationCodeRequestUrl authorizationUrl = this.googleAuthCodeFlow.newAuthorizationUrl().setRedirectUri(redirectUri);
                this.onAuthorization(authorizationUrl);
                String code = receiver.waitForCode();
                TokenResponse response = googleAuthCodeFlow.newTokenRequest(code).setRedirectUri(redirectUri).execute();
                Credential var7 = this.googleAuthCodeFlow.createAndStoreCredential(response, gmailEmail);
                log.info("Successfully created new Credential: {}", gmailEmail);
                return var7;
            }

            var3 = credential;
        } finally {
            receiver.stop();
        }

        return var3;
    }

    protected void onAuthorization(AuthorizationCodeRequestUrl authorizationUrl) throws IOException {
        String url = authorizationUrl.build();
        Preconditions.checkNotNull(url);
        this.browser.browse(url);
    }
}
