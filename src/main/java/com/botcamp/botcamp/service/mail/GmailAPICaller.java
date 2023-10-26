package com.botcamp.botcamp.service.mail;

import com.botcamp.botcamp.service.mail.query.Query;
import com.google.api.services.gmail.model.Message;

import java.io.IOException;
import java.util.List;

public interface GmailAPICaller {
     List<Message> callGmailAPI(GmailAPIAction apiAction, Query<?> query) throws InterruptedException, IOException;
     void sleep() throws InterruptedException;


}
