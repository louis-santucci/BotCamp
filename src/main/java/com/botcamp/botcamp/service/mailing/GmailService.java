package com.botcamp.botcamp.service.mailing;

import java.io.IOException;
import java.util.List;

public interface GmailService {
    List<Email> getBCEmails(String beginDate, String endDate) throws IOException;
    void sendEmail(Email email, String sender, String receiver);
}
