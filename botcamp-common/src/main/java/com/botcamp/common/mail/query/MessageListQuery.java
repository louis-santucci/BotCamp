package com.botcamp.common.mail.query;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MessageListQuery implements Query<GmailQueryParameter> {
    private String userEmail;
    private GmailQueryParameter query;
    private String nextPageToken;

    @Override
    public String getUserEmail() {
        return this.userEmail;
    }

    @Override
    public GmailQueryParameter getQueryObject() {
        return this.query;
    }
}
