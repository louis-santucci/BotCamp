package com.botcamp.common.mail;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MessageBody {
    private MessageBodyType type;
    private byte[] messageBody;
}
