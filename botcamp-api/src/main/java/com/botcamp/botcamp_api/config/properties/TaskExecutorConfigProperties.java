package com.botcamp.botcamp_api.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "task-executor")
public class TaskExecutorConfigProperties {
    private int queueCapacity;
    private int corePoolSize;
    private int maxPoolSize;
    private String pollingCron;
}
