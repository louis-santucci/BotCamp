package com.botcamp.gmail_gateway_api.credentials;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.gmail.Gmail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class GmailCredential {
    private String gmailEmail;
    @JsonIgnore
    private Gmail gmail;
    @JsonIgnore
    private Credential credential;
    private String credentialFilename;
    private String createdAt;
    private String expiresAt;
}
