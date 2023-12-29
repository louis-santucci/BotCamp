package com.botcamp.botcamp_api.config.http.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpRequestInterceptor;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j(topic = "[HTTP][REQUEST]")
@Component
public class LoggingRequestInterceptor implements HttpRequestInterceptor {

    @Override
    public void process(HttpRequest httpRequest, EntityDetails entityDetails, HttpContext httpContext) throws HttpException, IOException {
        log.info("{} Request sent [{}]", httpRequest.getMethod(), httpRequest.getRequestUri());
    }
}
