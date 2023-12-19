package com.botcamp.gmail_gateway_api.mailing.impl;

import com.botcamp.common.exception.EmailHandlingException;
import com.botcamp.common.mail.Email;
import com.botcamp.common.mail.MessageBody;
import com.botcamp.common.mail.MessageBodyType;
import com.botcamp.gmail_gateway_api.mailing.*;
import com.botcamp.common.utils.DateUtils;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME;

@Component
public class GmailMessageHandlerImpl implements MessageHandler {
    private static final String HEADER_TO = "To";
    private static final String HEADER_SUBJECT = "Subject";
    private static final String HEADER_FROM = "From";
    private static final String HEADER_DATE = "Date";
    private static final String TEXT_PLAIN = "text/plain";
    private static final String MULTIPART_ALTERNATIVE = "multipart/alternative";
    private static final String MULTIPART_MIXED = "multipart/mixed";

    public Email handleMessage(Object msg) throws EmailHandlingException {
        try {
            Message message = (Message) msg;
            Map<String, String> headerMap = headerListToMap(message.getPayload().getHeaders());
            String receiver = headerMap.get(HEADER_TO);
            String sender = getEmailFromHeaderValue(headerMap);
            String subject = headerMap.get(HEADER_SUBJECT);
            String dateTimeStr = DateUtils.cleanDate(headerMap.get(HEADER_DATE));
            LocalDateTime dateTime = DateUtils.stringToDateTime(dateTimeStr, RFC_1123_DATE_TIME);
            MessageBody messageBody = getMessageBody(message.getPayload());
            String base64BodyString = new String(Base64.getUrlDecoder().decode(messageBody.getMessageBody()));
            String body = cleanCrlfEndOfLine(base64BodyString);

            return Email.builder()
                    .receiver(receiver)
                    .subject(subject)
                    .sender(sender)
                    .dateTime(DateUtils.dateTimeToString(dateTime))
                    .body(body)
                    .bodyType(messageBody.getType())
                    .build();
        } catch (Exception e) {
            throw new EmailHandlingException(e);
        }
    }

    private static MessageBody getMessageBody(MessagePart messagePart) {
        byte[] base64Body = null;
        MessageBodyType type = MessageBodyType.TEXT_HTML;
        if (messagePart != null) {
            if (isMultipartMixed(messagePart)) {
                return getMessageBody(messagePart.getParts().get(0));
            }
            if (isMultipartAlternative(messagePart)) {
                List<MessagePart> parts = messagePart.getParts();
                Map<String, MessagePart> partMap = parts.stream().collect(Collectors.toMap(MessagePart::getMimeType, Function.identity()));
                if (partMap.containsKey(TEXT_PLAIN)) {
                    base64Body = partMap.get(TEXT_PLAIN).getBody().getData().getBytes(StandardCharsets.ISO_8859_1);
                    type = MessageBodyType.TEXT_PLAIN;
                } else {
                    base64Body =  parts.get(0).getBody().getData().getBytes(StandardCharsets.ISO_8859_1);
                }
            } else {
                base64Body = messagePart.getBody().getData().getBytes(StandardCharsets.ISO_8859_1);
            }
        }
        return new MessageBody(type, base64Body);
    }

    private static boolean isMultipartAlternative(MessagePart part) {
        return part.getMimeType().equalsIgnoreCase(MULTIPART_ALTERNATIVE);
    }

    private static boolean isMultipartMixed(MessagePart part) {
        return part.getMimeType().equalsIgnoreCase(MULTIPART_MIXED);
    }

    private static Map<String, String> headerListToMap(List<MessagePartHeader> list) {
        return list.stream().collect(Collectors.toMap(MessagePartHeader::getName, MessagePartHeader::getValue, (a, b) -> b));
    }

    private static String cleanCrlfEndOfLine(String text) {
        return text.replace("\\r\\n","\n");
    }

    private static String getEmailFromHeaderValue(Map<String, String> headerMap) {
        String headerValue = headerMap.get(HEADER_FROM);
        if (headerValue == null) {
            headerValue = headerMap.get(HEADER_FROM.toLowerCase());
        }
        return headerValue.substring(headerValue.indexOf('<') + 1, headerValue.indexOf('>'));
    }
}
