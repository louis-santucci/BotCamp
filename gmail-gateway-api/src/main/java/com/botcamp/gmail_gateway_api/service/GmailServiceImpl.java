package com.botcamp.gmail_gateway_api.service;

import com.botcamp.common.exception.EmailHandlingException;
import com.botcamp.common.exception.UnknownUserException;
import com.botcamp.common.mail.Email;
import com.botcamp.common.mail.EmailError;
import com.botcamp.gmail_gateway_api.config.GatewayUser;
import com.botcamp.common.mail.EmailResults;
import com.botcamp.gmail_gateway_api.mailing.GmailAPICaller;
import com.botcamp.gmail_gateway_api.mailing.MessageHandler;
import com.botcamp.common.mail.query.GmailQueryParameter;
import com.botcamp.common.mail.query.MessageListQuery;
import com.botcamp.common.mail.query.MessageQuery;
import com.google.api.services.gmail.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.botcamp.common.mail.GmailAPIAction.MESSAGE_GET;
import static com.botcamp.common.mail.GmailAPIAction.MESSAGE_LIST;


@Service
@Slf4j
public class GmailServiceImpl implements GmailService {

    private static final String BANDCAMP_EMAIL = "noreply@bandcamp.com";
    private static final String BANDCAMP_SUBJECT = "\"New Release From\"";
    private final GmailAPICaller gmailAPICaller;
    private final MessageHandler messageHandler;

    public GmailServiceImpl(GmailAPICaller gmailAPICaller,
                            MessageHandler messageHandler) {
        this.gmailAPICaller = gmailAPICaller;
        this.messageHandler = messageHandler;
    }

    @Override
    public EmailResults getEmails(GatewayUser gatewayUser, String beginDate, String endDate, String sender, String subject) throws IOException, InterruptedException, EmailHandlingException, UnknownUserException {
        String gmailEmail = gatewayUser.getGmailEmail();
        GmailQueryParameter query = GmailQueryParameter.builder()
                .beginDate(beginDate)
                .endDate(endDate)
                .from(sender)
                .subject(subject)
                .build();
        Pair<List<Email>, List<EmailError>> results = getEmails(gmailEmail, query);
        return new EmailResults(results.getLeft(), results.getRight());
    }

    private Pair<List<Email>, List<EmailError>> getEmails(String gmailEmail, GmailQueryParameter queryParameter) throws IOException, InterruptedException, EmailHandlingException, UnknownUserException {
        MessageListQuery messageListQuery = new MessageListQuery(gmailEmail, queryParameter, null);
        List<Message> results = gmailAPICaller.callGmailAPI(gmailEmail, MESSAGE_LIST, messageListQuery);

        List<Email> resultList = new ArrayList<>();
        List<EmailError> errorList = new ArrayList<>();
        for (Message result : results) {
            MessageQuery messageQuery = new MessageQuery(gmailEmail, result);
            Optional<Message> message = gmailAPICaller.callGmailAPI(gmailEmail, MESSAGE_GET, messageQuery).stream().findFirst();
            if (message.isPresent()) {
                Message msg = message.get();
                try {
                    Email email = messageHandler.handleMessage(msg);
                    resultList.add(email);
                } catch (EmailHandlingException e) {
                    log.error("Error on mail {}: \"{}\"", msg.getId(), e.getMessage());
                    EmailError error = new EmailError(msg.getId(), e.getMessage());
                    errorList.add(error);
                }
            }
        }

        log.info("Returned {} emails", resultList.size());
        if (!errorList.isEmpty()) {
            log.warn("Had {} errors getting emails", errorList.size());
        }

        return new ImmutablePair<>(resultList, errorList);
    }
}
