package com.botcamp.gmail_gateway_api.mailing.impl;

import com.botcamp.gmail_gateway_api.mailing.Email;
import com.botcamp.gmail_gateway_api.mailing.EmailHandlingException;
import com.botcamp.gmail_gateway_api.mailing.MessageHandler;
import com.botcamp.utils.DateUtils;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME;

@Component
public class GmailMessageHandlerImpl implements MessageHandler {
    private static final String HEADER_TO = "To";
    private static final String HEADER_SUBJECT = "Subject";
    private static final String HEADER_FROM = "From";
    private static final String HEADER_DATE = "Date";

    public Email handleMessage(Object msg) throws EmailHandlingException {
        try {
            Message message = (Message) msg;
            Map<String, String> headerMap = headerListToMap(message.getPayload().getHeaders());
            String receiver = headerMap.get(HEADER_TO);
            String sender = getEmailFromHeaderValue(headerMap.get(HEADER_FROM));
            String subject = headerMap.get(HEADER_SUBJECT);
            String dateTimeStr = DateUtils.cleanDate(headerMap.get(HEADER_DATE));
            LocalDateTime dateTime = DateUtils.StringToDateTime(dateTimeStr, RFC_1123_DATE_TIME);
            byte[] base64Body = getMessageBody(message);
            String base64BodyString = new String(Base64.getUrlDecoder().decode(base64Body));
            String body = cleanCrlfEndOfLine(base64BodyString);

            return Email.builder()
                    .receiver(receiver)
                    .subject(subject)
                    .sender(sender)
                    .dateTime(dateTime)
                    .body(body)
                    .build();
        } catch (Exception e) {
            throw new EmailHandlingException(e);
        }
    }

    private static byte[] getMessageBody(Message message) {
        List<MessagePart> parts = message.getPayload().getParts();
        byte[] base64Body;
        if (parts != null) {
            base64Body = message.getPayload().getParts().get(0).getBody().getData().getBytes(StandardCharsets.ISO_8859_1);
        } else {
            base64Body = message.getPayload().getBody().getData().getBytes(StandardCharsets.ISO_8859_1);
        }

        return base64Body;
    }

    private static Map<String, String> headerListToMap(List<MessagePartHeader> list) {
        return list.stream().collect(Collectors.toMap(MessagePartHeader::getName, MessagePartHeader::getValue, (a, b) -> b));
    }

    private static String cleanCrlfEndOfLine(String text) {
        return text.replaceAll("\\r\\n","\n");
    }

    private static String getEmailFromHeaderValue(String headerValue) {
        return headerValue.substring(headerValue.indexOf('<') + 1, headerValue.indexOf('>'));
    }
}
