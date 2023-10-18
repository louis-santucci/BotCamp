package com.botcamp.botcamp.service.mailing;

@FunctionalInterface
public interface MessageHandler {
    Email handleMessage(Object message);
}
