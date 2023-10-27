package com.botcamp.gmail_gateway_api.reporting;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.botcamp.gmail_gateway_api.MemoryAppender;
import com.botcamp.gmail_gateway_api.config.GmailAPICallerConfig;
import com.botcamp.gmail_gateway_api.config.GmailUserConfig;
import com.botcamp.gmail_gateway_api.configuration.TestConfiguration;
import com.botcamp.gmail_gateway_api.mailing.GmailAPIAction;
import com.botcamp.gmail_gateway_api.mailing.impl.GmailAPICallerImpl;
import com.botcamp.gmail_gateway_api.mailing.query.MessageQuery;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = TestConfiguration.class)
@ActiveProfiles("test")
@Slf4j
public class GmailAPICallerTests {

    @Autowired
    private GmailUserConfig userConfig;
    @Autowired
    private GmailAPICallerConfig apiCallerConfig;
    private GmailAPICallerImpl caller;
    Map<String, Message> messageMap;

    MemoryAppender memoryAppender;

    public GmailAPICallerTests() {
    }

    Gmail gmail;

    @BeforeEach
    public void setup() {
        Logger logger = (Logger) LoggerFactory.getLogger(GmailAPICallerImpl.class.getName());
        memoryAppender = new MemoryAppender();
        memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        logger.setLevel(Level.DEBUG);
        logger.addAppender(memoryAppender);
        memoryAppender.start();
    }

    @AfterEach
    public void cleanup() {
        this.memoryAppender.reset();
    }


    private void configureTests(int messageListSize) throws IOException {

        this.messageMap = generateMessages(messageListSize);

        // Gmail Mock config
        gmail = mock(Gmail.class);
        Gmail.Users users = mock(Gmail.Users.class);
        Gmail.Users.Messages messages = mock(Gmail.Users.Messages.class);
        Map<String, Gmail.Users.Messages.Get> getMap = new HashMap<>();
        messageMap.forEach((key, value) -> {
            Gmail.Users.Messages.Get get = mock(Gmail.Users.Messages.Get.class);
            get.setUserId(userConfig.getEmail());
            get.setId(key);
            getMap.put(key, get);
            try {
                doReturn(messageMap.get(key)).when(get).execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        doReturn(users).when(gmail).users();
        doReturn(messages).when(users).messages();
        for (Map.Entry<String, Message> entry: messageMap.entrySet()) {
            String id = entry.getKey();
            doReturn(getMap.get(id)).when(messages).get(userConfig.getEmail(), id);
        }

        this.caller = spy(new GmailAPICallerImpl(gmail, apiCallerConfig));
    }

    private Map<String, Message> generateMessages(int size) {
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

    @Test
    public void should_log_and_reset_twice() throws InterruptedException, IOException {

        configureTests(101);

        AtomicInteger counter = new AtomicInteger(1);
        int size = messageMap.size();
        this.messageMap.forEach((key, value) -> {
            log.info("#" + counter.getAndIncrement() + " - call to API");
            MessageQuery query = new MessageQuery(userConfig.getEmail(), value);
            try {
                this.caller.callGmailAPI(GmailAPIAction.MESSAGE_GET, query);
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        });
        verify(this.caller, atLeast(2)).sleep();
    }

    @Test
    public void should_reset_between_each_call() throws IOException, InterruptedException {
        configureTests(5);

        AtomicInteger counter = new AtomicInteger(1);
        this.messageMap.forEach((key, value) -> {
            log.info("#" + counter.getAndIncrement() + " - call to API");
            MessageQuery query = new MessageQuery(userConfig.getEmail(), value);
            try {
                this.caller.callGmailAPI(GmailAPIAction.MESSAGE_GET, query);
                Thread.sleep(6000);
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        });
        Long occurences = memoryAppender.countOccurences("Limit reset to 0", Level.DEBUG);
        assertThat(occurences).isGreaterThan(5);
    }
}
