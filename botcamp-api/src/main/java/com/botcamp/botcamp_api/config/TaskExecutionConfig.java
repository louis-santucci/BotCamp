package com.botcamp.botcamp_api.config;

import com.botcamp.botcamp_api.config.properties.TaskExecutorConfigProperties;
import com.botcamp.botcamp_api.config.properties.TaskSchedulerConfigProperties;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import javax.sql.DataSource;

@Configuration
@Import({
        TaskExecutorConfigProperties.class,
        TaskSchedulerConfigProperties.class
})
@EnableSchedulerLock(defaultLockAtMostFor = "10m")
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

    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(JdbcTemplateLockProvider.Configuration.builder()
                .withJdbcTemplate(new JdbcTemplate(dataSource))
                .usingDbTime()
                .withDbUpperCase(true)
                .build());
    }
}
