package com.botcamp.botcamp_api.config.http;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class HeaderBuilderInterceptor implements Interceptor {

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request req = chain.request();
        Request headerReq = req
                .newBuilder()
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .build();

        return chain.proceed(headerReq);
    }
}
