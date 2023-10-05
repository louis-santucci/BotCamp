package com.botcamp.botcamp.service.mailing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GmailQuery {

    private static final char SPACE = ' ';
    private static final String BEGIN_DATE = "beginDate";
    private static final String END_DATE = "endDate";
    private static final char COLON = ':';

    private String from;
    private String beginDate;
    private String endDate;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (from != null) sb.append("from:").append(this.from);
        if (beginDate != null) {
            if (!sb.isEmpty()) sb.append(SPACE);
            sb.append(BEGIN_DATE).append(COLON).append(this.beginDate);
        }
        if (endDate != null) {
            if (!sb.isEmpty()) sb.append(SPACE);
            sb.append(END_DATE).append(COLON).append(this.endDate);
        }
        return sb.toString();
    }
}
