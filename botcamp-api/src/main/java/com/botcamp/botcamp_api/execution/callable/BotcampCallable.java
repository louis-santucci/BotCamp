package com.botcamp.botcamp_api.execution.callable;

import com.botcamp.botcamp_api.execution.Execution;
import lombok.Getter;

import java.util.concurrent.Callable;

@Getter
public abstract class BotcampCallable<T> implements Callable<T> {

    private final Execution execution;

    protected BotcampCallable(Execution execution) {
        this.execution = execution;
    }
}
