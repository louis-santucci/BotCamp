package com.botcamp.common.mail;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageBodyType {
    TEXT_PLAIN("plain"),
    TEXT_HTML("html");

    @JsonValue
    private final String type;

    @Override
    public String toString() {
        return this.name();
    }
}
