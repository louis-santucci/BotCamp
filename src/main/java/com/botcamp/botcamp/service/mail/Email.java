package com.botcamp.botcamp.service.mail;

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
    private String subject;
    private String receiver;
    private LocalDateTime dateTime;
}
