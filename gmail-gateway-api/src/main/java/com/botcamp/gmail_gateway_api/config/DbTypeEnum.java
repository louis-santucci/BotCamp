package com.botcamp.gmail_gateway_api.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DbTypeEnum {
    H2("h2", "jdbc:h2:file:"),
    POSTGRES("postgres","jdbc:postgresql://%s:%d/%s");

    private final String type;
    private final String url;

    public static DbTypeEnum fromString(String text) {
        for (DbTypeEnum type : DbTypeEnum.values()) {
            if (type.type.equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null;
    }
}
