package com.botcamp.botcamp_api.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "task-scheduler")
public class TaskSchedulerConfigProperties {
    private int poolSize;
}
