package com.botcamp.botcamp.service.mailing;

import java.io.IOException;
import java.util.List;

public interface GmailService {
    List<Email> getEmails(String beginDate, String endDate, String sender, String subject) throws IOException, InterruptedException;
}
