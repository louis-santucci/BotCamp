package com.botcamp.gmail_gateway_api.mailing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
public class MessageBody {
    private MessageBodyType type;
    private byte[] messageBody;
}
