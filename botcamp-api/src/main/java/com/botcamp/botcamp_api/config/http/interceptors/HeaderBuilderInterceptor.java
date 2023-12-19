package com.botcamp.botcamp_api.config.http.interceptors;

import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpRequestInterceptor;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HeaderBuilderInterceptor implements HttpRequestInterceptor {

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";

    @Override
    public void process(HttpRequest httpRequest, EntityDetails entityDetails, HttpContext httpContext) throws HttpException, IOException {
        httpRequest.setHeader(CONTENT_TYPE, APPLICATION_JSON);
    }
}
