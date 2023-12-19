package com.botcamp.botcamp_api.config.http.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpResponseInterceptor;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j(topic = "[HTTP][RESPONSE]")
@Component
public class LoggingResponseInterceptor implements HttpResponseInterceptor {

    @Override
    public void process(HttpResponse httpResponse, EntityDetails entityDetails, HttpContext httpContext) throws HttpException, IOException {
        log.info("Response received [STATUS: {}]", httpResponse.getCode());
    }
}
