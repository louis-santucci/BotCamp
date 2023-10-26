package com.botcamp.botcamp.utils;

import com.google.api.services.gmail.model.Message;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MessageUtils {
    public static Map<String, Message> generateMessages(final int size) {
        List<Message> messageList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Message msg = new Message();
            String randomId = RandomStringUtils.randomAlphanumeric(10);
            msg.setId(randomId);
            messageList.add(msg);
        }
        return messageList
                .stream()
                .collect(Collectors.toMap(Message::getId, Function.identity()));
    }
}
