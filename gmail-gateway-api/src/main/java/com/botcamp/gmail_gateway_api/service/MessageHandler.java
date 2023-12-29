package com.botcamp.gmail_gateway_api.service;

import com.botcamp.common.exception.EmailHandlingException;
import com.botcamp.common.mail.Email;

@FunctionalInterface
public interface MessageHandler {
    Email handleMessage(Object message) throws EmailHandlingException;
}
