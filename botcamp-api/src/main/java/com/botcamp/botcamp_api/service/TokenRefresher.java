package com.botcamp.botcamp_api.service;

import com.botcamp.botcamp_api.config.http.HttpResponseHandler;
import com.botcamp.botcamp_api.config.properties.GmailGatewayCredentialsProperties;
import com.botcamp.botcamp_api.config.properties.GmailGatewayProperties;
import com.botcamp.common.endpoints.GmailGatewayEndpoint;
import com.botcamp.common.jwt.JwtToken;
import com.botcamp.common.request.JwtRequest;
import com.botcamp.common.response.GenericResponse;
import com.botcamp.common.response.JwtResponse;
import com.botcamp.common.utils.HttpUtils;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class TokenRefresher {

    private JwtToken token;
    private final HttpClientBuilder httpClientBuilder;
    private final ObjectMapper objectMapper;
    private final GmailGatewayProperties gatewayProperties;

    public TokenRefresher(@Qualifier(value = "basicHttpClientBuilder") HttpClientBuilder httpClientBuilder,
                          ObjectMapper objectMapper,
                          GmailGatewayProperties gatewayProperties) {
        this.objectMapper = objectMapper;
        this.gatewayProperties = gatewayProperties;
        this.httpClientBuilder = httpClientBuilder;
        this.token = null;
    }

    public JwtToken provideJwtToken() throws IOException {
        // if expiration < 10s or token is null, refresh
        if (token == null || token.getExpiresAt().minusSeconds(10).isBefore(LocalDateTime.now())) {
            token = requestNewToken();
            return token;
        }
        // else get token
        return token;
    }

    private JwtToken requestNewToken() throws IOException {
        String endpoint = GmailGatewayEndpoint.API_AUTH + GmailGatewayEndpoint.AUTH;
        String url = HttpUtils.buildUrl(gatewayProperties.getProtocol(), gatewayProperties.getUrl(), gatewayProperties.getPort(), endpoint);
        GmailGatewayCredentialsProperties credentials = gatewayProperties.getCredentials();
        JwtRequest jwtRequest = new JwtRequest(credentials.getUsername(), credentials.getPassword());
        String jsonInput = objectMapper.writeValueAsString(jwtRequest);
        try (CloseableHttpClient httpClient = httpClientBuilder.build();
             StringEntity stringEntity = new StringEntity(jsonInput)) {
            HttpPost post = HttpUtils.buildHttpPost(url, stringEntity);

            HttpResponseHandler responseHandler = new HttpResponseHandler();

            String jsonOutput = httpClient.execute(post, responseHandler);
            JavaType jwtResponseType = objectMapper.getTypeFactory().constructParametricType(GenericResponse.class, JwtResponse.class);
            GenericResponse<JwtResponse> genericResponse = objectMapper.readValue(jsonOutput, jwtResponseType);

            return new JwtToken(genericResponse.getData());
        }
    }
}
