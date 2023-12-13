package com.botcamp.gmail_gateway_api.service;

import com.botcamp.common.exception.EmailParsingException;
import com.botcamp.common.utils.DateUtils;
import com.botcamp.common.utils.EmailUtils;
import com.botcamp.gmail_gateway_api.config.properties.GoogleOAuth2ConfigProperties;
import com.botcamp.gmail_gateway_api.credentials.GmailCredential;
import com.botcamp.gmail_gateway_api.credentials.GmailCredentialProvider;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.services.gmail.Gmail;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class GmailCredentialsServiceImpl implements GmailCredentialsService {

    private final Map<String, GmailCredential> credentialsMap;
    private final String applicationName;
    private final GoogleAuthorizationCodeFlow googleAuthCodeFlow;
    private final GoogleOAuth2ConfigProperties googleOAuth2ConfigProperties;
    private final JsonFactory gsonFactory;
    private final HttpTransport httpTransport;
    private final GmailCredentialProvider credentialProvider;

    public GmailCredentialsServiceImpl(ApplicationContext context,
                                       GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow,
                                       HttpTransport httpTransport,
                                       JsonFactory gsonFactory,
                                       GoogleOAuth2ConfigProperties googleOAuth2ConfigProperties,
                                       GmailCredentialProvider credentialProvider) throws EmailParsingException, IOException {
        this.credentialsMap = new HashMap<>();
        this.applicationName = context.getId();
        this.googleAuthCodeFlow = googleAuthorizationCodeFlow;
        this.httpTransport = httpTransport;
        this.gsonFactory = gsonFactory;
        this.googleOAuth2ConfigProperties = googleOAuth2ConfigProperties;
        this.credentialProvider = credentialProvider;

        initGmailCredentialsMap();
    }

    @Override
    public void initGmailCredentialsMap() throws IOException, EmailParsingException {
        DataStore<StoredCredential> credentialDataStore = this.googleAuthCodeFlow.getCredentialDataStore();
        for (String email: credentialDataStore.keySet()) {
            credentialsMap.put(email, createGmailCredential(email));
        }
    }

    @Override
    public Map<String, GmailCredential> getGmailCredentials() {
        return this.credentialsMap;
    }


    @Override
    public GmailCredential createGmailCredential(String gmailEmail) throws EmailParsingException, IOException {
        Credential credential = getCredentials(gmailEmail);
        Gmail gmail = buildGmailClient(credential);
        LocalDateTime now = LocalDateTime.now();
        String dateTimeNow = DateUtils.dateTimeToString(now);
        String expirationTime = DateUtils.dateTimeToString(now.plusSeconds(credential.getExpiresInSeconds()));
        GmailCredential gmailCredential = GmailCredential.builder()
                .credentialFilename(googleOAuth2ConfigProperties.getStoredCredentialsFileName())
                .gmail(gmail)
                .gmailEmail(gmailEmail)
                .credential(credential)
                .createdAt(dateTimeNow)
                .expiresAt(expirationTime)
                .build();

        credentialsMap.put(gmailCredential.getGmailEmail(), gmailCredential);
        return gmailCredential;
    }

    private Gmail buildGmailClient(Credential credential) {
        return new Gmail.Builder(httpTransport, gsonFactory, credential)
                .setApplicationName(applicationName)
                .build();
    }

    private Credential getCredentials(final String gmailEmail) throws IOException, EmailParsingException {
        EmailUtils.validateEmail(gmailEmail);
        return credentialProvider.authorize(gmailEmail);
    }
}
