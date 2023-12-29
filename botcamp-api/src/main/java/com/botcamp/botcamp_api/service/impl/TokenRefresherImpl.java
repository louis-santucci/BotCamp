package com.botcamp.botcamp_api.service.impl;

import com.botcamp.botcamp_api.service.GmailGatewayClient;
import com.botcamp.botcamp_api.service.TokenRefresher;
import com.botcamp.common.jwt.JwtToken;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class TokenRefresherImpl implements TokenRefresher {

    private JwtToken token;
    private final GmailGatewayClient gatewayClient;

    public TokenRefresherImpl(GmailGatewayClient client) {
        this.gatewayClient = client;
        this.token = null;
    }

    @Override
    public JwtToken provideJwtToken() throws IOException {
        // if expiration < 10s or token is null, refresh
        if (token == null || token.getExpiresAt().minusSeconds(10).isBefore(LocalDateTime.now())) {
            token = gatewayClient.requestNewToken();
            return token;
        }
        // else get token
        return token;
    }
}
