package com.botcamp.botcamp_api.config;

import com.botcamp.botcamp_api.config.properties.HttpConfigProperties;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.time.Duration;

@Configuration
@Import(HttpConfigProperties.class)
public class HttpConfig {
    @Bean
    OkHttpClient okHttpClient(HttpConfigProperties httpConfigProperties) {
        OkHttpClient client = new OkHttpClient.Builder()
                .callTimeout(Duration.ofSeconds(httpConfigProperties.getRequestTimeout()))
                .build();
    }
}
