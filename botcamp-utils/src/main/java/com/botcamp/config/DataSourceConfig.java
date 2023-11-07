package com.botcamp.config;

import com.botcamp.config.properties.DataSourceConfigProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

@Configuration
@Import(DataSourceConfigProperties.class)
public class DataSourceConfig {

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
