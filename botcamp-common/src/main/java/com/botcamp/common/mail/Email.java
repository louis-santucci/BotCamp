package com.botcamp.common.mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Email {
    private String sender;
    private String body;
    private MessageBodyType bodyType;
    private String subject;
    private String receiver;
    private String dateTime;
}
