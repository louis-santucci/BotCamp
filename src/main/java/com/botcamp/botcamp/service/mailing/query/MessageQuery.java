package com.botcamp.botcamp.service.mailing.query;

import com.google.api.services.gmail.model.Message;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public class MessageQuery implements Query<Message> {
    private String userEmail;
    private Message message;

    @Override
    public String getUserEmail() {
        return this.userEmail;
    }

    @Override
    public Message getQueryObject() {
        return this.message;
    }
}
