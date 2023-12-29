package com.botcamp.gmail_gateway_api.config;


import com.botcamp.gmail_gateway_api.config.properties.GoogleOAuth2ConfigProperties;
import com.botcamp.gmail_gateway_api.credentials.Browser;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.FileDataStoreFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.List;

@Configuration
@Import(GoogleOAuth2ConfigProperties.class)
public class GmailAPIOAuthConfiguration {

    private static final String ACCESS_TYPE_OFFLINE = "offline";
    private static final String APPROVAL_PROMPT_FORCE = "force";

    @Bean
    GsonFactory gsonFactory() {
        return GsonFactory.getDefaultInstance();
    }

    @Bean
    NetHttpTransport netHttpTransport() throws GeneralSecurityException, IOException {
        return GoogleNetHttpTransport.newTrustedTransport();
    }

    @Bean
    FileDataStoreFactory fileDataStoreFactory(GoogleOAuth2ConfigProperties googleOAuth2ConfigProperties) throws IOException {
        File dirPath = new File(googleOAuth2ConfigProperties.getTokenDirectoryName());

        return new FileDataStoreFactory(dirPath);
    }

    @Bean
    GoogleAuthorizationCodeFlow googleAuthCodeFlow(HttpTransport httpTransport,
                                                   JsonFactory gsonFactory,
                                                   GoogleOAuth2ConfigProperties googleOAuth2ConfigProperties,
                                                   GoogleClientSecrets googleClientSecrets,
                                                   FileDataStoreFactory fileDataStoreFactory) throws IOException {
        DataStore<StoredCredential> credentialDataStore = fileDataStoreFactory.getDataStore(googleOAuth2ConfigProperties.getStoredCredentialsFileName());
        List<String> scopes = googleOAuth2ConfigProperties.getScopes();
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow
                .Builder(httpTransport, gsonFactory, googleClientSecrets, scopes)
                .setCredentialDataStore(credentialDataStore)
                .setAccessType(ACCESS_TYPE_OFFLINE)
                .setApprovalPrompt(APPROVAL_PROMPT_FORCE)
                .build();


        return flow;
    }

    @Bean
    AuthorizationCodeInstalledApp.Browser browser() {
        return new Browser();
    }

    @Bean
    GoogleClientSecrets googleClientSecrets(GsonFactory gsonFactory, GoogleOAuth2ConfigProperties googleOAuth2ConfigProperties) throws IOException {
        String credentialsFilePath = googleOAuth2ConfigProperties.getCredentialsFilePath();
        InputStream in = GmailAPIOAuthConfiguration.class.getResourceAsStream(credentialsFilePath);
        if (in == null) throw new FileNotFoundException("Client Secret file not found: " + credentialsFilePath);

        return GoogleClientSecrets.load(gsonFactory, new InputStreamReader(in));
    }

}
