package com.botcamp.botcamp_api.config.http;

import com.botcamp.botcamp_api.config.properties.HttpConfigProperties;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(HttpConfigProperties.class)
public class HttpConfig {
    @Bean
    OkHttpClient okHttpClient(HttpConfigProperties httpConfigProperties,
                              HeaderBuilderInterceptor headerBuilderInterceptor,
                              HeaderAuthInterceptor headerAuthInterceptor,
                              RetryInterceptor retryInterceptor) {
        return new OkHttpClient.Builder()
                .readTimeout(httpConfigProperties.getRequestTimeout(), httpConfigProperties.getUnit())
                .addInterceptor(headerBuilderInterceptor)
                .addInterceptor(headerAuthInterceptor)
                .addInterceptor(retryInterceptor)
                .build();
    }
}
