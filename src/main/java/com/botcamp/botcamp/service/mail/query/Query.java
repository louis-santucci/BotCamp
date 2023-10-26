package com.botcamp.botcamp.service.mail.query;

public interface Query<T> {
    String getUserEmail();
    T getQueryObject();
}
