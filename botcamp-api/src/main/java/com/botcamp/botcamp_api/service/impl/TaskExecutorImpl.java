package com.botcamp.botcamp_api.service.impl;

import com.botcamp.botcamp_api.execution.Execution;
import com.botcamp.botcamp_api.execution.callable.LogCallable;
import com.botcamp.botcamp_api.service.TaskExecutionService;
import com.botcamp.botcamp_api.service.TaskExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import static com.botcamp.botcamp_api.execution.ExecutionStatus.*;
import static com.botcamp.common.request.SortingOrderParameter.ASCENDING;

@Component
@Slf4j
public class TaskExecutorImpl implements TaskExecutor {

    private final TaskExecutionService taskExecutionService;
    private final ThreadPoolTaskExecutor executor;

    public TaskExecutorImpl(TaskExecutionService executionService,
                            @Qualifier("botcampExecutor") ThreadPoolTaskExecutor executor) {
        this.taskExecutionService = executionService;
        this.executor = executor;
    }

    @Override
    @Scheduled(cron = "${task-executor.polling-cron}")
    public void poll() {
        List<Execution> pendingExecutions = taskExecutionService.getExecutions(
                null,
                null,
                Collections.singletonList(PENDING),
                null,
                ASCENDING,
                null,
                false);
        if (pendingExecutions.isEmpty()) return;
        int size = pendingExecutions.size();
        log.info("Adding {} executions to queue", size);
        executeTasks(pendingExecutions);
    }

    @Override
    public void executeTasks(List<Execution> pendingExecutions) {
        int successfulExecutions = 0;
        for (Execution exec : pendingExecutions) {
            try {
                executor.submit(new LogCallable(exec)).get();
                this.taskExecutionService.updateExecution(exec.getId(), SUCCESS);
                successfulExecutions++;
            } catch (InterruptedException | ExecutionException e) {
                log.error("Error while executing callable: {}", e.getMessage());
                this.taskExecutionService.updateExecution(exec.getId(), FAILURE);
            }
        }
        log.info("Successfully executed {} executions", successfulExecutions);
    }

}
