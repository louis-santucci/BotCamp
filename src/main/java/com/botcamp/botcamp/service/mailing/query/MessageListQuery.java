package com.botcamp.botcamp.service.mailing.query;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public class MessageListQuery implements Query<GmailQueryParameter> {
    private String userEmail;
    private GmailQueryParameter query;

    @Override
    public String getUserEmail() {
        return this.userEmail;
    }

    @Override
    public GmailQueryParameter getQueryObject() {
        return this.query;
    }
}
