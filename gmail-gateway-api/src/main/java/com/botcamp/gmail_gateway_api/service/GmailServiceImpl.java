package com.botcamp.gmail_gateway_api.service;

import com.botcamp.gmail_gateway_api.config.properties.GmailUserConfigProperties;
import com.botcamp.gmail_gateway_api.mailing.Email;
import com.botcamp.gmail_gateway_api.mailing.GmailAPICaller;
import com.botcamp.gmail_gateway_api.mailing.MessageHandler;
import com.botcamp.gmail_gateway_api.mailing.query.GmailQueryParameter;
import com.botcamp.gmail_gateway_api.mailing.query.MessageListQuery;
import com.botcamp.gmail_gateway_api.mailing.query.MessageQuery;
import com.google.api.services.gmail.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.botcamp.gmail_gateway_api.mailing.GmailAPIAction.MESSAGE_GET;
import static com.botcamp.gmail_gateway_api.mailing.GmailAPIAction.MESSAGE_LIST;


@Service
@Slf4j
public class GmailServiceImpl implements GmailService {

    private static final String BANDCAMP_EMAIL = "noreply@bandcamp.com";
    private static final String BANDCAMP_SUBJECT = "\"New Release From\"";
    private final GmailAPICaller gmailAPICaller;
    private GmailUserConfigProperties userConfig;
    private MessageHandler messageHandler;

    public GmailServiceImpl(GmailUserConfigProperties gmailUserConfigProperties,
                            GmailAPICaller gmailAPICaller,
                            MessageHandler messageHandler) {
        this.gmailAPICaller = gmailAPICaller;
        this.userConfig = gmailUserConfigProperties;
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
