package com.botcamp.gmail_gateway_api.mailing.query;

public interface Query<T> {
    String getUserEmail();
    T getQueryObject();
}
