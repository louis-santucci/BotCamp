package com.botcamp.gmail_gateway_api.mailing;

import com.botcamp.common.exception.UnknownUserException;
import com.botcamp.gmail_gateway_api.mailing.query.Query;
import com.google.api.services.gmail.model.Message;

import java.io.IOException;
import java.util.List;

public interface GmailAPICaller {
     List<Message> callGmailAPI(String gmailEmail, GmailAPIAction apiAction, Query<?> query) throws InterruptedException, IOException, UnknownUserException;
     void sleep() throws InterruptedException;


}
