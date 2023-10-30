package com.botcamp.gmail_gateway_api.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties
@Import({
        GmailUserConfig.class,
        GmailAPICallerConfig.class,
        DataSourceConfigProperties.class,
        GmailAPIOAuthConfiguration.class})
public class GmailGatewayConfiguration {

    private static final String ENV_FILE = ".env";
    private static final String DB_URL = "jdbc:postgresql://%s:%d/%s";

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        Resource resource = new ClassPathResource(ENV_FILE);
        configurer.setLocation(resource);

        return configurer;
    }

    @Bean
    public DataSource dataSource(DataSourceConfigProperties config) {
        String url = String.format(DB_URL, config.getHostname(), config.getPort(), config.getDbName());
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder
                .create()
                .username(config.getUsername())
                .password(config.getPassword())
                .url(url);

        return dataSourceBuilder.build();
    }
}
