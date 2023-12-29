package com.botcamp.common.mail.query;

public interface Query<T> {
    String getUserEmail();
    T getQueryObject();
}
