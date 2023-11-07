package com.botcamp.gmail_gateway_api.mailing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private LocalDateTime dateTime;
}
