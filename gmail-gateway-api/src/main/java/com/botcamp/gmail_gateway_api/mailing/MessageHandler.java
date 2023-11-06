package com.botcamp.gmail_gateway_api.mailing;

@FunctionalInterface
public interface MessageHandler {
    Email handleMessage(Object message) throws EmailHandlingException;
}
