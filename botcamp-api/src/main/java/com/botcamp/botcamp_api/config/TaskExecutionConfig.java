package com.botcamp.botcamp_api.config;

import com.botcamp.botcamp_api.config.properties.TaskExecutorConfigProperties;
import com.botcamp.botcamp_api.config.properties.TaskSchedulerConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@Import({
        TaskExecutorConfigProperties.class,
        TaskSchedulerConfigProperties.class
})
public class TaskExecutionConfig {

    private static final String TASK_SCHEDULER = "task-scheduler";
    private static final String TASK_EXECUTOR = "task-executor";

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(TaskSchedulerConfigProperties schedulerConfigProperties) {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(schedulerConfigProperties.getPoolSize());
        scheduler.setThreadNamePrefix(TASK_SCHEDULER);
        scheduler.setWaitForTasksToCompleteOnShutdown(true);

        return scheduler;
    }

    @Bean(name = "botcampExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(TaskExecutorConfigProperties executorConfigProperties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(executorConfigProperties.getCorePoolSize());
        executor.setMaxPoolSize(executorConfigProperties.getMaxPoolSize());
        executor.setQueueCapacity(executor.getQueueCapacity());
        executor.setThreadNamePrefix(TASK_EXECUTOR);
        return executor;
    }
}
