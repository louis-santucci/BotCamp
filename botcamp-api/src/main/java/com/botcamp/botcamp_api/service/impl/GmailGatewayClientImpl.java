package com.botcamp.botcamp_api.service.impl;

import com.botcamp.botcamp_api.config.properties.GmailGatewayCredentialsProperties;
import com.botcamp.botcamp_api.config.properties.GmailGatewayProperties;
import com.botcamp.botcamp_api.service.GmailGatewayClient;
import com.botcamp.common.endpoints.GmailGatewayEndpoint;
import com.botcamp.common.jwt.JwtToken;
import com.botcamp.common.mail.EmailResults;
import com.botcamp.common.request.JwtRequest;
import com.botcamp.common.response.GenericResponse;
import com.botcamp.common.response.JwtResponse;
import com.botcamp.common.utils.GzipUtils;
import com.botcamp.common.utils.HttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static com.botcamp.common.endpoints.GmailGatewayEndpoint.*;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Component
public class GmailGatewayClientImpl implements GmailGatewayClient {

    private static final String AUTH_ENDPOINT = GmailGatewayEndpoint.API_AUTH + GmailGatewayEndpoint.AUTH;
    private static final String EMAIL_ENDPOINT = GmailGatewayEndpoint.API_MAIL + GmailGatewayEndpoint.GET_LIST;

    private final ObjectMapper objectMapper;
    private final RestTemplate authRestTemplate;
    private final RestTemplate basicRestTemplate;
    private final GmailGatewayProperties gmailGatewayProperties;

    public GmailGatewayClientImpl(ObjectMapper objectMapper,
                                  @Qualifier(value = "basicRestTemplate") RestTemplate basicRestTemplate,
                                  @Qualifier(value = "authRestTemplate") RestTemplate authRestTemplate,
                                  GmailGatewayProperties gatewayProperties) {
        this.objectMapper = objectMapper;
        this.authRestTemplate = authRestTemplate;
        this.basicRestTemplate = basicRestTemplate;
        this.gmailGatewayProperties = gatewayProperties;
    }

    @Override
    public JwtToken requestNewToken() {
        String url = HttpUtils.buildUrl(gmailGatewayProperties.getProtocol(), gmailGatewayProperties.getUrl(), gmailGatewayProperties.getPort(), AUTH_ENDPOINT);
        GmailGatewayCredentialsProperties credentials = gmailGatewayProperties.getCredentials();
        JwtRequest jwtRequest = new JwtRequest(credentials.getUsername(), credentials.getPassword());
        HttpEntity<JwtRequest> httpEntity = new HttpEntity<>(jwtRequest);
        ResponseEntity<GenericResponse<JwtResponse>> response = basicRestTemplate
                .exchange(url, POST, httpEntity, buildParameterizedJwtResponse());
        GenericResponse<JwtResponse> responseBody = response.getBody();

        return responseBody != null ? new JwtToken(responseBody.getData()) : null;
    }

    @Override
    public EmailResults getEmails(String beginDate,
                                  String endDate,
                                  String sender,
                                  String subject,
                                  boolean compress) throws IOException {
        String url = HttpUtils.buildUrl(gmailGatewayProperties.getProtocol(), gmailGatewayProperties.getUrl(), gmailGatewayProperties.getPort(), EMAIL_ENDPOINT);
        MultiValueMap<String, String> params = buildQueryParameters(beginDate, endDate, sender, subject, compress);
        String uri = UriComponentsBuilder
                .fromHttpUrl(url)
                .queryParams(params)
                .encode().toUriString();
        if (compress) {
            ResponseEntity<GenericResponse<byte[]>> response = authRestTemplate
                    .exchange(uri, GET, null, buildParameterizedCompressedGzipResults());

            GenericResponse<byte[]> responseBody = response.getBody();
            String decompressedString = responseBody != null ? decompressGzip(responseBody.getData()) : null;
            return decompressedString != null ? objectMapper.readValue(decompressedString, EmailResults.class) : null;
        }
        ResponseEntity<GenericResponse<EmailResults>> response = authRestTemplate
                .exchange(uri, GET, null, buildParameterizedEmailResults());

        GenericResponse<EmailResults> responseBody = response.getBody();

        return responseBody != null ? responseBody.getData() : null;
    }

    private MultiValueMap<String, String> buildQueryParameters(String beginDate,
                                                               String endDate,
                                                               String sender,
                                                               String subject,
                                                               boolean compress) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        if (beginDate != null) queryParams.add(BEGIN_DATE_QUERY_PARAM, beginDate);
        if (endDate != null) queryParams.add(END_DATE_QUERY_PARAM, endDate);
        if (sender != null) queryParams.add(SENDER_QUERY_PARAM, sender);
        if (subject != null) queryParams.add(SUBJECT_QUERY_PARAM, subject);
        if (compress) queryParams.add(COMPRESS_QUERY_PARAM, Boolean.TRUE.toString());
        return queryParams;
    }

    private String decompressGzip(byte[] byteArray) throws IOException {
        return GzipUtils.decompress(byteArray);
    }

    private ParameterizedTypeReference<GenericResponse<JwtResponse>> buildParameterizedJwtResponse() {
        return new ParameterizedTypeReference<>() {
        };
    }

    private ParameterizedTypeReference<GenericResponse<EmailResults>> buildParameterizedEmailResults() {
        return new ParameterizedTypeReference<>() {
        };
    }

    private ParameterizedTypeReference<GenericResponse<byte[]>> buildParameterizedCompressedGzipResults() {
        return new ParameterizedTypeReference<>() {
        };
    }
}
