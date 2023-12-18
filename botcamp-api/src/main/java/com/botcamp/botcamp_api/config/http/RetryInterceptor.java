package com.botcamp.botcamp_api.config.http;

import com.botcamp.botcamp_api.config.properties.HttpConfigProperties;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class RetryInterceptor implements Interceptor {

    private final int maxRetries;
    private final Long retryDelay;

    public RetryInterceptor(HttpConfigProperties httpConfigProperties) {
        this.maxRetries = httpConfigProperties.getMaxRetries();
        this.retryDelay = httpConfigProperties.getRetryDelay();
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request req = chain.request();
        // Tries the request
        Response response = chain.proceed(req);
        int tryCount = 0;
        while (!response.isSuccessful() && tryCount < maxRetries) {
            tryCount++;
            log.warn("Request unsuccessful, retrying... (retry n°{})", tryCount);
            try {
                Thread.sleep(retryDelay * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            response.close();
            response = chain.proceed(req);
        }

        // Otherwise
        return response;
    }
}
