package com.botcamp.botcamp_api.config.http.interceptors;

import com.botcamp.botcamp_api.service.TokenRefresher;
import com.botcamp.common.jwt.JwtToken;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpRequestInterceptor;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HeaderAuthInterceptor implements HttpRequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final TokenRefresher tokenRefresher;

    public HeaderAuthInterceptor(@Lazy TokenRefresher tokenRefresher) {
        this.tokenRefresher = tokenRefresher;
    }

    @Override
    public void process(HttpRequest httpRequest, EntityDetails entityDetails, HttpContext httpContext) throws HttpException, IOException {
        JwtToken jwtToken = tokenRefresher.provideJwtToken();
        if (jwtToken != null) {
            httpRequest.addHeader(AUTHORIZATION_HEADER, jwtToken.getToken());
        }
    }
}
