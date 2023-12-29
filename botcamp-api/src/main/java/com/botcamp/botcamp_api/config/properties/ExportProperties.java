package com.botcamp.botcamp_api.config.properties;

import com.botcamp.botcamp_api.execution.callable.exporter.ExportType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties("export")
public class ExportProperties {
    private String path;
    private List<ExportType> types;
}
