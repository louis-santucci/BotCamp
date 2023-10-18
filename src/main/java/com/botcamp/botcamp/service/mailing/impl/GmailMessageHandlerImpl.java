package com.botcamp.botcamp.service.mailing.impl;

import com.botcamp.botcamp.service.mailing.Email;
import com.botcamp.botcamp.service.mailing.MessageHandler;
import com.botcamp.botcamp.utils.DateUtils;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GmailMessageHandlerImpl implements MessageHandler {
    private static final String HEADER_TO = "To";
    private static final String HEADER_SUBJECT = "Subject";
    private static final String HEADER_FROM = "From";
    private static final String HEADER_DATE = "Date";
    private static final String HEADER_DATETIME_FORMAT = "dd LLL yyyy HH:mm:ss";

    public Email handleMessage(Object msg) {
        Message message = (Message) msg;
        String base64Body = message.getPayload().getParts().get(0).getBody().getData();
        String body = cleanCrlfEndOfLine(new String(Base64.getDecoder().decode(base64Body)));
        Map<String, String> headerMap = headerListToMap(message.getPayload().getHeaders());

        String receiver = getEmailFromHeaderValue(headerMap.get(HEADER_TO));
        String sender = getEmailFromHeaderValue(headerMap.get(HEADER_FROM));
        String subject = headerMap.get(HEADER_SUBJECT);
        String dateTimeStr = DateUtils.cleanDate(headerMap.get(HEADER_DATE));
        LocalDateTime dateTime = DateUtils.StringToDateTime(dateTimeStr, HEADER_DATETIME_FORMAT);

        Email email = Email.builder()
                .receiver(receiver)
                .subject(subject)
                .sender(sender)
                .dateTime(dateTime)
                .body(body)
                .build();
        return email;
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
