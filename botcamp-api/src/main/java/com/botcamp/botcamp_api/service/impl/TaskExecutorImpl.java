package com.botcamp.botcamp_api.service.impl;

import com.botcamp.botcamp_api.config.properties.ExportProperties;
import com.botcamp.botcamp_api.execution.Execution;
import com.botcamp.botcamp_api.execution.callable.GmailEmailHandlerCallable;
import com.botcamp.botcamp_api.execution.callable.exporter.CallableExporter;
import com.botcamp.botcamp_api.execution.callable.exporter.EmailExporter;
import com.botcamp.botcamp_api.execution.callable.exporter.ExportType;
import com.botcamp.botcamp_api.execution.callable.exporter.FileExporter;
import com.botcamp.botcamp_api.service.EmailSender;
import com.botcamp.botcamp_api.service.GmailGatewayClient;
import com.botcamp.botcamp_api.service.TaskExecutionService;
import com.botcamp.botcamp_api.service.TaskExecutor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.botcamp.botcamp_api.execution.ExecutionStatus.*;
import static com.botcamp.common.request.SortingOrderParameter.ASCENDING;

@Component
@Slf4j
public class TaskExecutorImpl implements TaskExecutor {

    private final TaskExecutionService taskExecutionService;
    private final ThreadPoolTaskExecutor executor;
    private final List<CallableExporter> exporters;
    private final GmailGatewayClient client;
    private final EmailSender emailSender;

    public TaskExecutorImpl(TaskExecutionService executionService,
                            @Qualifier("botcampExecutor") ThreadPoolTaskExecutor executor,
                            ExportProperties exportProperties,
                            GmailGatewayClient client,
                            EmailSender emailSender) throws IOException {
        this.taskExecutionService = executionService;
        this.executor = executor;
        this.exporters = initExporters(exportProperties);
        this.client = client;
        this.emailSender = emailSender;
    }

    List<CallableExporter> initExporters(ExportProperties exportProperties) throws IOException {
        List<CallableExporter> exporterList = new ArrayList<>();
        for (ExportType type : exportProperties.getTypes()) {
            if (type == ExportType.FILE_EXPORT) {
                exporterList.add(new FileExporter(exportProperties, taskExecutionService));
            } else if (type == ExportType.EMAIL_EXPORT) {
                exporterList.add(new EmailExporter(exportProperties, emailSender));
            }
        }

        return exporterList;
    }

    @Override
    @Scheduled(cron = "${task-executor.polling-cron}")
    @SchedulerLock(name = "taskExecutionPolling")
    public void poll() throws ExecutionException, InterruptedException {
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
    public void executeTasks(List<Execution> pendingExecutions) throws InterruptedException, ExecutionException {
        int successfulExecutions = 0;
        for (Execution exec : pendingExecutions) {
            try {
                taskExecutionService.updateExecutionStatus(exec.getId(), RUNNING);
                Future<Void> future = executor.submit(new GmailEmailHandlerCallable(exec, client, exporters));
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                log.error("Error while executing callable: {}", e.getMessage());
                this.taskExecutionService.updateExecutionStatus(exec.getId(), FAILURE);
                throw e;
            }
            this.taskExecutionService.updateExecutionStatus(exec.getId(), SUCCESS);
            successfulExecutions++;
        }
        log.info("Successfully executed {} executions", successfulExecutions);
    }

}
