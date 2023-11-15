package com.botcamp.common.config;

import com.botcamp.common.config.properties.CorsConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@Import(CorsConfigProperties.class)
public class CorsConfig {

    @Bean
    @Primary
    CorsConfigurationSource corsConfigurationSource(CorsConfigProperties corsConfigProperties) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(corsConfigProperties.getAllowedOrigins());
        configuration.setAllowedMethods(corsConfigProperties.getAllowedMethods());
        configuration.setAllowedHeaders(corsConfigProperties.getAllowedHeaders());
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(corsConfigProperties.getPattern(), configuration);
        return source;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CorsFilter corsFilter(CorsConfigurationSource source) {
        return new CorsFilter(source);
    }
}
