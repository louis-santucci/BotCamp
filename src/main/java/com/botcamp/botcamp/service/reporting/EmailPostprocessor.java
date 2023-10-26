package com.botcamp.botcamp.service.reporting;

import com.botcamp.botcamp.service.mailing.Email;

public interface EmailPostprocessor<T extends Record> {
    T processEmail(Email email);
    Class<T> getClazz();
}
