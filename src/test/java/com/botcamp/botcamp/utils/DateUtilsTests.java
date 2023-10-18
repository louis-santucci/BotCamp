package com.botcamp.botcamp.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class DateUtilsTests {
    private static final String DATE_TEST_1 = "30 Sep 2023 09:02:58";
    private static final String DATE_CLEAN_TEST_1 = "Sat, 30 Sep 2023 09:02:58 +0000 (UTC)";
    private static final String FORMAT_1 = "dd MMM yyyy HH:mm:ss";

    @Test
    public void test_date_clean() {
        String expectedDateStr = "30 Sep 2023 09:02:58";
        String actualDateTime = DateUtils.cleanDate(DATE_CLEAN_TEST_1);

        assertThat(actualDateTime).isEqualTo(expectedDateStr);
    }

    @Test
    public void test_date_time() {
        LocalDateTime expectedDateTime = LocalDateTime.of(2023, 9, 30, 11, 2, 58);
        LocalDateTime actualDateTime = DateUtils.StringToDateTime(DATE_TEST_1, FORMAT_1);

        assertThat(actualDateTime).isEqualTo(expectedDateTime);
    }
}
