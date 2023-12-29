package com.botcamp.botcamp_api.config.http;

import com.botcamp.botcamp_api.config.http.interceptors.HeaderAuthInterceptor;
import com.botcamp.botcamp_api.config.http.interceptors.HeaderBuilderInterceptor;
import com.botcamp.botcamp_api.config.http.interceptors.LoggingRequestInterceptor;
import com.botcamp.botcamp_api.config.http.interceptors.LoggingResponseInterceptor;
import com.botcamp.botcamp_api.config.properties.HttpConfigProperties;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(HttpConfigProperties.class)
public class HttpConfig {

    @Bean
    ConnectionConfig connectionConfig(HttpConfigProperties properties) {
        return ConnectionConfig.custom()
                .setConnectTimeout(properties.getConnectionTimeout(), properties.getConnectionTimeoutUnit())
                .setSocketTimeout(properties.getSocketTimeout(), properties.getSocketTimeoutUnit())
                .build();
    }

    @Bean
    BasicHttpClientConnectionManager httpClientConnectionManager(ConnectionConfig connectionConfig) {
        BasicHttpClientConnectionManager basicHttpClientConnectionManager = new BasicHttpClientConnectionManager();
        basicHttpClientConnectionManager.setConnectionConfig(connectionConfig);

        return basicHttpClientConnectionManager;
    }

    @Bean(value = "authHttpClientBuilder")
    HttpClientBuilder authHttpClientBuilder(HttpConfigProperties httpConfigProperties,
                                        HttpClientConnectionManager httpClientConnectionManager,
                                        LoggingRequestInterceptor loggingRequestInterceptor,
                                        LoggingResponseInterceptor loggingResponseInterceptor,
                                        HeaderBuilderInterceptor headerBuilderInterceptor,
                                        HeaderAuthInterceptor headerAuthInterceptor) {
        HttpClientBuilder httpClientBuilder = HttpClients.custom()
                .setConnectionManager(httpClientConnectionManager);

        httpClientBuilder.addRequestInterceptorLast(headerBuilderInterceptor);
        httpClientBuilder.addRequestInterceptorLast(headerAuthInterceptor);
        if (httpConfigProperties.isLoggingEnabled()) {
            httpClientBuilder.addRequestInterceptorLast(loggingRequestInterceptor);
            httpClientBuilder.addResponseInterceptorLast(loggingResponseInterceptor);
        }


        return httpClientBuilder;
    }

    @Bean(value = "basicHttpClientBuilder")
    HttpClientBuilder basicHttpClientBuilder(HttpConfigProperties httpConfigProperties,
                                        HttpClientConnectionManager httpClientConnectionManager,
                                        LoggingRequestInterceptor loggingRequestInterceptor,
                                        LoggingResponseInterceptor loggingResponseInterceptor,
                                        HeaderBuilderInterceptor headerBuilderInterceptor) {
        HttpClientBuilder httpClientBuilder = HttpClients.custom()
                .setConnectionManager(httpClientConnectionManager);

        httpClientBuilder.addRequestInterceptorLast(headerBuilderInterceptor);
        if (httpConfigProperties.isLoggingEnabled()) {
            httpClientBuilder.addRequestInterceptorLast(loggingRequestInterceptor);
            httpClientBuilder.addResponseInterceptorLast(loggingResponseInterceptor);
        }


        return httpClientBuilder;
    }
}
