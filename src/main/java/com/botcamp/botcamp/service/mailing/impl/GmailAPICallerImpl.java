package com.botcamp.botcamp.service.mailing.impl;

import com.botcamp.botcamp.config.GmailAPICallerConfig;
import com.botcamp.botcamp.service.mailing.GmailAPIAction;
import com.botcamp.botcamp.service.mailing.GmailAPICaller;
import com.botcamp.botcamp.service.mailing.query.MessageListQuery;
import com.botcamp.botcamp.service.mailing.query.MessageQuery;
import com.botcamp.botcamp.service.mailing.query.Query;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Data
public class GmailAPICallerImpl implements GmailAPICaller {
    private Gmail gmail;
    private GmailAPICallerConfig apiConfig;

    private int limit;


    public GmailAPICallerImpl(Gmail gmail, GmailAPICallerConfig apiConfig) {
        this.gmail = gmail;
        this.limit = 0;
        this.apiConfig = apiConfig;
    }

    @Scheduled(cron = "${gmail.api.caller.reset-cron}")
    public void resetLimit() {
        log.debug("Limit reset to 0");
        this.limit = 0;
    }

    @Override
    public synchronized List<Message> callGmailAPI(GmailAPIAction apiAction, Query<?> queryObject) throws InterruptedException, IOException {
        if (this.limit >= apiConfig.getCostLimit()) sleep();

        limit += apiAction.getCost();
        return switch (apiAction) {
            case MESSAGE_GET -> {
                log.debug("Getting message from Gmail API");
                MessageQuery messageQuery = (MessageQuery) queryObject;
                Message msg = getMessage(messageQuery);
                yield Collections.singletonList(msg);
            }
            case MESSAGE_LIST -> {
                log.debug("Getting message list from Gmail API");
                MessageListQuery messageListQuery = (MessageListQuery) queryObject;
                yield getMessageList(messageListQuery);
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

    private List<Message> getMessageList(MessageListQuery messageListQuery) throws IOException, InterruptedException {
        ListMessagesResponse listMessagesResponse = this.gmail
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
            List<Message> newResults = this.callGmailAPI(GmailAPIAction.MESSAGE_LIST, messageListQuery);
            messageList.addAll(newResults);
        }

        return messageList;
    }

    private Message getMessage(MessageQuery messageQuery) throws IOException {
        Gmail.Users.Messages.Get get = this.gmail.users().messages().get(messageQuery.getUserEmail(), messageQuery.getQueryObject().getId());
        return get.execute();
    }
}
