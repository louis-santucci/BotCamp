package com.botcamp.gmail_gateway_api.mailing.impl;

import com.botcamp.common.exception.UnknownUserException;
import com.botcamp.gmail_gateway_api.config.properties.GmailAPICallerProperties;
import com.botcamp.common.gateway_credentials.GmailCredential;
import com.botcamp.common.mail.GmailAPIAction;
import com.botcamp.gmail_gateway_api.mailing.GmailAPICaller;
import com.botcamp.common.mail.query.MessageListQuery;
import com.botcamp.common.mail.query.MessageQuery;
import com.botcamp.common.mail.query.Query;
import com.botcamp.gmail_gateway_api.service.GmailCredentialsService;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
@Component
public class GmailAPICallerImpl implements GmailAPICaller {
    private GmailCredentialsService credentialsService;
    private GmailAPICallerProperties apiConfig;

    private int limit;


    public GmailAPICallerImpl(GmailCredentialsService credentialsService, GmailAPICallerProperties apiConfig) {
        this.credentialsService = credentialsService;
        this.limit = 0;
        this.apiConfig = apiConfig;
    }

    @Scheduled(cron = "${gmail.api.caller.reset-cron}")
    public void resetLimit() {
        log.debug("Limit reset to 0");
        this.limit = 0;
    }

    @Override
    public synchronized List<Message> callGmailAPI(String gmailEmail, GmailAPIAction apiAction, Query<?> queryObject) throws InterruptedException, IOException, UnknownUserException {
        if (this.limit >= apiConfig.getCostLimit()) sleep();
        limit += apiAction.getCost();

        Map<String, GmailCredential> gmailCredentials = credentialsService.getGmailCredentials();

        if (gmailCredentials.get(gmailEmail) == null) {
            throw new UnknownUserException("User is not registered in Gmail Credentials");
        }

        return switch (apiAction) {
            case MESSAGE_GET -> {
                log.debug("Getting message from Gmail API");
                MessageQuery messageQuery = (MessageQuery) queryObject;
                Message msg = getMessage(gmailEmail, messageQuery);
                yield Collections.singletonList(msg);
            }
            case MESSAGE_LIST -> {
                log.debug("Getting message list from Gmail API");
                MessageListQuery messageListQuery = (MessageListQuery) queryObject;
                yield getMessageList(gmailEmail, messageListQuery);
            }
            case MESSAGE_TRASH, MESSAGE_SEND -> {
                log.warn("NOT IMPLEMENTED YET");
                yield new ArrayList<>(0);
            }
        };
    }

    @Override
    public void sleep() throws InterruptedException {
        Thread.sleep(apiConfig.getCostFrequency());
        resetLimit();
    }

    private List<Message> getMessageList(String gmailEmail, MessageListQuery messageListQuery) throws IOException, InterruptedException, UnknownUserException {
        Gmail gmail = credentialsService.getGmailCredentials().get(gmailEmail).getGmail();

        ListMessagesResponse listMessagesResponse = gmail
                .users()
                .messages()
                .list(messageListQuery.getUserEmail())
                .setPageToken(messageListQuery.getNextPageToken())
                .setQ(messageListQuery.getQueryObject().toString())
                .setMaxResults(apiConfig.getMaxResults())
                .execute();
        List<Message> messageList = listMessagesResponse.getMessages();
        String nextToken = listMessagesResponse.getNextPageToken();
        if (nextToken != null) {
            messageListQuery.setNextPageToken(nextToken);
            List<Message> newResults = this.callGmailAPI(gmailEmail, GmailAPIAction.MESSAGE_LIST, messageListQuery);
            messageList.addAll(newResults);
        }
        log.info("Requesting {} emails", messageList.size());
        return messageList;
    }

    private Message getMessage(String gmailEmail, MessageQuery messageQuery) throws IOException {
        Gmail gmail = credentialsService.getGmailCredentials().get(gmailEmail).getGmail();
        Long start = System.currentTimeMillis();
        Gmail.Users.Messages.Get get = gmail.users().messages().get(messageQuery.getUserEmail(), messageQuery.getQueryObject().getId());
        Message message =  get.execute();
        Long end = System.currentTimeMillis();
        log.info("Got email with id {} ({}ms)", message.getId(), end - start);
        return message;
    }
}
