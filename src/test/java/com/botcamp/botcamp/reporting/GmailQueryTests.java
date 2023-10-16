package com.botcamp.botcamp.reporting;

import com.botcamp.botcamp.service.mailing.query.GmailQuery;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GmailQueryTests {

    private static final String TOTO_MAIL = "toto@toto.com";
    private static final String SUBJECT = "\"New message from Toto\"";
    private static final String BEGIN_DATE = "2023/01/01";
    private static final String END_DATE = "2023/04/20";

    @Test
    public void valid_to_string_without_dates() {
        GmailQuery query = GmailQuery.builder()
                .from(TOTO_MAIL)
                .build();
        String result = String.format("from:%s", TOTO_MAIL);
        assertThat(query.toString()).isEqualTo(result);
    }

    @Test
    public void valid_to_string_without_from() {
        String result = String.format("after:%s before:%s", BEGIN_DATE, END_DATE);
        GmailQuery query = GmailQuery.builder()
                .beginDate(BEGIN_DATE)
                .endDate(END_DATE)
                .build();
        assertThat(query.toString()).isEqualTo(result);
    }

    @Test
    public void valid_to_string_with_all() {
        String result = String.format("from:%s after:%s before:%s", TOTO_MAIL, BEGIN_DATE, END_DATE);
        GmailQuery query = GmailQuery.builder()
                .from(TOTO_MAIL)
                .beginDate(BEGIN_DATE)
                .endDate(END_DATE)
                .build();
        assertThat(query.toString()).isEqualTo(result);
    }

    @Test
    public void valid_to_string_with_subjct_only() {
        String result = String.format("subject:%s", SUBJECT);
        GmailQuery query = GmailQuery.builder()
                .subject("\"New message from Toto\"")
                .build();
        assertThat(query.toString()).isEqualTo(result);
    }
}
