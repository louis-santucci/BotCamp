package com.botcamp.botcamp.service.mailing;

import com.google.api.services.gmail.model.Message;

@FunctionalInterface
public interface MessageHandler {
    Email handleMessage(Message message);
}
