package com.botcamp.gmail_gateway_api.mailing.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GmailQueryParameter {

    private static final char SPACE = ' ';
    private static final String AFTER = "after";
    private static final String BEFORE = "before";
    private static final String SUBJECT = "subject";
    private static final char COLON = ':';

    private String from;
    private String beginDate;
    private String endDate;
    private String subject;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (from != null) sb.append("from:").append(this.from);
        if (beginDate != null) {
            if (!sb.isEmpty()) sb.append(SPACE);
            sb.append(AFTER).append(COLON).append(this.beginDate);
        }
        if (endDate != null) {
            if (!sb.isEmpty()) sb.append(SPACE);
            sb.append(BEFORE).append(COLON).append(this.endDate);
        }
        if (subject != null) {
            if (!sb.isEmpty()) sb.append(SPACE);
            sb.append(SUBJECT).append(COLON).append(this.subject);
        }
        return sb.toString();
    }
}
