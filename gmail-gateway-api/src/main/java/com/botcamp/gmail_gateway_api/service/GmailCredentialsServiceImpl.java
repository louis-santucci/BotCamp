package com.botcamp.gmail_gateway_api.service;

import com.botcamp.common.exception.EmailParsingException;
import com.botcamp.common.utils.DateUtils;
import com.botcamp.common.utils.EmailUtils;
import com.botcamp.gmail_gateway_api.config.properties.GoogleOAuth2ConfigProperties;
import com.botcamp.gmail_gateway_api.credentials.GmailCredential;
import com.botcamp.gmail_gateway_api.credentials.GmailCredentialFactory;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.services.gmail.Gmail;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class GmailCredentialsServiceImpl implements GmailCredentialsService {

    private final Map<String, GmailCredential> credentialsMap;
    private final String applicationName;
    private final GoogleAuthorizationCodeFlow googleAuthCodeFlow;
    private final GoogleOAuth2ConfigProperties googleOAuth2ConfigProperties;
    private final JsonFactory gsonFactory;
    private final HttpTransport httpTransport;
    private final GmailCredentialFactory credentialFactory;

    public GmailCredentialsServiceImpl(ApplicationContext context,
                                       GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow,
                                       HttpTransport httpTransport,
                                       JsonFactory gsonFactory,
                                       GoogleOAuth2ConfigProperties googleOAuth2ConfigProperties,
                                       GmailCredentialFactory credentialFactory) throws EmailParsingException, IOException {
        this.credentialsMap = new HashMap<>();
        this.applicationName = context.getId();
        this.googleAuthCodeFlow = googleAuthorizationCodeFlow;
        this.httpTransport = httpTransport;
        this.gsonFactory = gsonFactory;
        this.googleOAuth2ConfigProperties = googleOAuth2ConfigProperties;
        this.credentialFactory = credentialFactory;

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

    @Override
    public void clearGmailCredentials(Boolean deleteFile) throws IOException {
        credentialsMap.clear();
        googleAuthCodeFlow.getCredentialDataStore().clear();
        log.info("Cleared Credentials");
        if (deleteFile) {
            File tokenDirectory = new File(googleOAuth2ConfigProperties.getTokenDirectoryName());
            FileUtils.cleanDirectory(tokenDirectory);
            log.info("Deleted credentials file {} in {} folder", googleOAuth2ConfigProperties.getCredentialsFilePath(), googleOAuth2ConfigProperties.getTokenDirectoryName());
        }
    }

    private Gmail buildGmailClient(Credential credential) {
        return new Gmail.Builder(httpTransport, gsonFactory, credential)
                .setApplicationName(applicationName)
                .build();
    }

    private Credential getCredentials(final String gmailEmail) throws IOException, EmailParsingException {
        EmailUtils.validateEmail(gmailEmail);
        return credentialFactory.buildCredential(gmailEmail);
    }
}
