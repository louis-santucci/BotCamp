package com.botcamp.botcamp_api.execution.callable;

import com.botcamp.botcamp_api.execution.Execution;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

@Slf4j
public class LogCallable extends BotcampCallable<Void> {

    public LogCallable(Execution execution) {
        super(execution);
    }

    @Override
    public Void call() throws Exception {
        Execution execution = getExecution();
        log.info("Execution: {}", execution);
        Thread.sleep(10000);
        return null;
    }
}
