package com.botcamp.botcamp_api.config.http;

import com.botcamp.botcamp_api.config.http.interceptors.HeaderAuthInterceptor;
import com.botcamp.botcamp_api.config.http.interceptors.HeaderBuilderInterceptor;
import com.botcamp.botcamp_api.config.http.interceptors.LoggingInterceptor;
import com.botcamp.botcamp_api.config.properties.HttpConfigProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;

@Configuration
@Import(HttpConfigProperties.class)
public class HttpConfig {
    @Bean(value = "basicRestTemplate")
    RestTemplate basicRestTemplate(HttpConfigProperties properties,
                                   HeaderBuilderInterceptor headerBuilderInterceptor,
                                   LoggingInterceptor loggingInterceptor) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(Duration.of(properties.getConnectionTimeout(), properties.getConnectionTimeoutUnit().toChronoUnit()))
                .setReadTimeout(Duration.of(properties.getSocketTimeout(), properties.getSocketTimeoutUnit().toChronoUnit()))
                .build();

        restTemplate.setInterceptors(List.of(headerBuilderInterceptor, loggingInterceptor));

        return restTemplate;
    }

    @Bean(value = "authRestTemplate")
    RestTemplate authRestTemplate(HttpConfigProperties properties,
                                  HeaderAuthInterceptor headerAuthInterceptor,
                                  HeaderBuilderInterceptor headerBuilderInterceptor,
                                  LoggingInterceptor loggingInterceptor) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(Duration.of(properties.getConnectionTimeout(), properties.getConnectionTimeoutUnit().toChronoUnit()))
                .setReadTimeout(Duration.of(properties.getSocketTimeout(), properties.getSocketTimeoutUnit().toChronoUnit()))
                .build();

        restTemplate.setInterceptors(List.of(headerBuilderInterceptor, headerAuthInterceptor, loggingInterceptor));

        return restTemplate;
    }
}
