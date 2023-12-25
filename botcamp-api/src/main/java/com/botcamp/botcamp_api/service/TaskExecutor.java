package com.botcamp.botcamp_api.service;

import com.botcamp.botcamp_api.execution.Execution;

import java.util.List;

public interface TaskExecutor {

    void poll();

    void executeTasks(List<Execution> executionList);
}
