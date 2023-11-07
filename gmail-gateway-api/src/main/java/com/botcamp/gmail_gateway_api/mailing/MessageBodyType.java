package com.botcamp.gmail_gateway_api.mailing;

import com.botcamp.gmail_gateway_api.config.DbTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageBodyType {
    TEXT_PLAIN,
    HTML
}
