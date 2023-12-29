package com.botcamp.botcamp_api.config.http.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j(topic = "[HTTP]")
@Component
public class LoggingInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            log.info("[{}][{}] Request sent", request.getMethodValue(), request.getURI());
            ClientHttpResponse response = execution.execute(request, body);
            log.info("[STATUS {}] Response received", response.getRawStatusCode(), response.getStatusText());
            return response;
    }
}
