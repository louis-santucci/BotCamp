package com.botcamp.botcamp_api.config.http.interceptors;

import com.botcamp.botcamp_api.service.TokenRefresher;
import com.botcamp.common.jwt.JwtToken;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HeaderAuthInterceptor implements ClientHttpRequestInterceptor {

    private final TokenRefresher tokenRefresher;

    public HeaderAuthInterceptor(@Lazy TokenRefresher tokenRefresher) {
        this.tokenRefresher = tokenRefresher;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        JwtToken jwtToken = tokenRefresher.provideJwtToken();
        if (jwtToken != null) {
            request.getHeaders().add(HttpHeaders.AUTHORIZATION, buildBearerToken(jwtToken.getToken()));
        }

        return execution.execute(request, body);
    }

    private String buildBearerToken(String token) {
        return "Bearer " + token;
    }
}
