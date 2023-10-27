package com.botcamp.botcamp.mail;

import com.botcamp.botcamp.config.GmailUserConfig;
import com.botcamp.botcamp.configuration.BotCampTestConfiguration;
import com.botcamp.botcamp.service.mail.GmailAPIAction;
import com.botcamp.botcamp.service.mail.GmailAPICaller;
import com.botcamp.botcamp.service.mail.GmailService;
import com.botcamp.botcamp.service.mail.MessageHandler;
import com.botcamp.botcamp.service.mail.impl.GmailServiceImpl;
import com.botcamp.botcamp.service.mail.query.Query;
import com.botcamp.botcamp.utils.MessageUtils;
import com.google.api.services.gmail.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = BotCampTestConfiguration.class)
@ActiveProfiles("test")
@Slf4j
public class GmailServiceTests {

    @Autowired
    private GmailUserConfig gmailUserConfig;
    @Autowired
    private MessageHandler messageHandler;
    private final GmailAPICaller apiCaller = Mockito.mock(GmailAPICaller.class);
    private GmailService gmailService;
    private Map<String, Message> messageMap = MessageUtils.generateMessages(10);

    @BeforeEach
    public void setUp() throws IOException, InterruptedException {
        initializeMock();
        this.gmailService = new GmailServiceImpl(gmailUserConfig, apiCaller, messageHandler);
    }

    private void initializeMock() throws IOException, InterruptedException {
        doReturn(messageMap).when(apiCaller).callGmailAPI(GmailAPIAction.MESSAGE_LIST, any(Query.class));
    }
}
