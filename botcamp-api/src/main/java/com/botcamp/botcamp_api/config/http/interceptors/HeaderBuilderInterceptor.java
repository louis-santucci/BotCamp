package com.botcamp.botcamp_api.config.http.interceptors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HeaderBuilderInterceptor implements ClientHttpRequestInterceptor {

    private static final String APPLICATION_JSON = "application/json";

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON);
        return execution.execute(request, body);
    }
}
