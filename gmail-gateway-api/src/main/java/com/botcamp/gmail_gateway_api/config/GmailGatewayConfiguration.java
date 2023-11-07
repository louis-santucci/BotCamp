package com.botcamp.gmail_gateway_api.config;

import com.botcamp.config.properties.DataSourceConfigProperties;
import com.botcamp.gmail_gateway_api.config.properties.GmailUserConfigProperties;
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
        GmailUserConfigProperties.class,
        GmailAPICallerConfig.class,
        DataSourceConfigProperties.class,
        GmailAPIOAuthConfiguration.class,
        WebSecurityConfig.class})
public class GmailGatewayConfiguration {

    private static final String ENV_FILE = ".env";

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        Resource resource = new ClassPathResource(ENV_FILE);
        configurer.setLocation(resource);

        return configurer;
    }

    @Bean
    public DataSource dataSource(DataSourceConfigProperties config) {
        DbTypeEnum type = DbTypeEnum.fromString(config.getDbType());
        if (type == null) throw new TypeNotPresentException(config.getDbType(), null);

        String url = switch (type) {
            case POSTGRES -> String.format(
                    config.getDbUrl(),
                    config.getHostname(),
                    config.getPort(),
                    config.getDbName());
            case H2 -> config.getDbUrl();
        };

        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder
                .create()
                .username(config.getUsername())
                .password(config.getPassword())
                .url(url);

        return dataSourceBuilder.build();
    }
}
