package com.botcamp.botcamp.service.mail;

@FunctionalInterface
public interface MessageHandler {
    Email handleMessage(Object message);
}
