package com.botcamp.botcamp_api.service;

import com.botcamp.botcamp_api.execution.Execution;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface TaskExecutor {

    void poll() throws ExecutionException, InterruptedException;

    void executeTasks(List<Execution> executionList) throws InterruptedException, ExecutionException;
}
