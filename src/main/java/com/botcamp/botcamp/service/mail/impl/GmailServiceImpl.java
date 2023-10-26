package com.botcamp.botcamp.service.mail.impl;

import com.botcamp.botcamp.config.GmailUserConfig;
import com.botcamp.botcamp.service.mail.Email;
import com.botcamp.botcamp.service.mail.GmailAPICaller;
import com.botcamp.botcamp.service.mail.GmailService;
import com.botcamp.botcamp.service.mail.MessageHandler;
import com.botcamp.botcamp.service.mail.query.GmailQueryParameter;
import com.botcamp.botcamp.service.mail.query.MessageListQuery;
import com.botcamp.botcamp.service.mail.query.MessageQuery;
import com.google.api.services.gmail.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.botcamp.botcamp.service.mail.GmailAPIAction.MESSAGE_GET;
import static com.botcamp.botcamp.service.mail.GmailAPIAction.MESSAGE_LIST;


@Service
@Slf4j
public class GmailServiceImpl implements GmailService {

    private static final String BANDCAMP_EMAIL = "noreply@bandcamp.com";
    private static final String BANDCAMP_SUBJECT = "\"New Release From\"";
    private final GmailAPICaller gmailAPICaller;
    private GmailUserConfig userConfig;
    private MessageHandler messageHandler;

    public GmailServiceImpl(GmailUserConfig gmailUserConfig,
                            GmailAPICaller gmailAPICaller,
                            MessageHandler messageHandler) {
        this.gmailAPICaller = gmailAPICaller;
        this.userConfig = gmailUserConfig;
        this.messageHandler = messageHandler;
    }

    @Override
    public List<Email> getEmails(String beginDate, String endDate, String sender, String subject) throws IOException, InterruptedException {
        GmailQueryParameter query = GmailQueryParameter.builder()
                .beginDate(beginDate)
                .endDate(endDate)
                .from(sender)
                .subject(subject)
                .build();

        return getEmails(query);
    }

    private List<Email> getEmails(GmailQueryParameter queryParameter) throws IOException, InterruptedException {
        String userEmail = userConfig.getEmail();
        MessageListQuery messageListQuery = new MessageListQuery(userEmail, queryParameter, null);
        List<Message> results = gmailAPICaller.callGmailAPI(MESSAGE_LIST, messageListQuery);

        List<Email> resultList = new ArrayList<>();
        for (Message result : results) {
            MessageQuery messageQuery = new MessageQuery(userEmail, result);
            Optional<Message> message = gmailAPICaller.callGmailAPI(MESSAGE_GET, messageQuery).stream().findFirst();
            if (message.isPresent()) {
                Email email = messageHandler.handleMessage(message.get());;
                resultList.add(email);
            }
        }
        log.info("Returned " + resultList.size() + " emails");

        return resultList;
    }
}
