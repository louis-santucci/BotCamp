package com.botcamp.botcamp.service.mailing.query;

public interface Query<T> {
    String getUserEmail();
    T getQueryObject();
}
