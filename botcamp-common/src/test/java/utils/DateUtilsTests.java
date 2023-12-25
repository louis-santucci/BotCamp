package utils;

import com.botcamp.common.utils.DateUtils;
import com.botcamp.common.utils.OffsetPair;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME;

class DateUtilsTests {
    private static final String DATE_TEST_1 = "Sat, 30 Sep 2023 09:02:58 +0000";
    private static final String DATE_TEST_2 = "Sat, 30 Sep 2023 09:02:58 -0200";
    private static final String DATE_TEST_3 = "Sat, 30 Sep 2023 09:02:58 +0730";

    private static final String DATE_CLEAN_TEST_1 = "Sat, 30 Sep 2023 09:02:58 +0000 (UTC)";
    private static final String DATE_CLEAN_TEST_2 = "Thu,  7 Sep 2023 09:02:58 +0000 (UTC)";


    @Test
    void test_date_clean() {
        String expectedDateStr = "Sat, 30 Sep 2023 09:02:58 +0000";
        String actualDateTime = DateUtils.cleanDate(DATE_CLEAN_TEST_1);

        Assertions.assertThat(actualDateTime).isEqualTo(expectedDateStr);
    }

    @Test
    void test_date_clean_2() {
        String expectedDateStr = "Thu, 7 Sep 2023 09:02:58 +0000";
        String actualDateTime = DateUtils.cleanDate(DATE_CLEAN_TEST_2);

        Assertions.assertThat(actualDateTime).isEqualTo(expectedDateStr);
    }


    @Test
    void test_date_time() {
        LocalDateTime expectedDateTime = LocalDateTime.of(2023, 9, 30, 11, 2, 58);
        LocalDateTime actualDateTime = DateUtils.stringToLocalDateTime(DATE_TEST_1, RFC_1123_DATE_TIME);

        Assertions.assertThat(actualDateTime).isEqualTo(expectedDateTime);
    }

    @Test
    void test_date_time_with_neg_offset() {
        LocalDateTime expectedDateTime = LocalDateTime.of(2023, 9, 30, 9, 2, 58);
        LocalDateTime actualDateTime = DateUtils.stringToLocalDateTime(DATE_TEST_2, RFC_1123_DATE_TIME);

        Assertions.assertThat(actualDateTime).isEqualTo(expectedDateTime);
    }

    @Test
    void test_date_time_with_pos_offset() {
        LocalDateTime expectedDateTime = LocalDateTime.of(2023, 9, 30, 18, 32, 58);
        LocalDateTime actualDateTime = DateUtils.stringToLocalDateTime(DATE_TEST_3, RFC_1123_DATE_TIME);

        Assertions.assertThat(actualDateTime).isEqualTo(expectedDateTime);
    }

    @Test
    void test_offset_parsing() {
        String offsetStr = "+0230";
        OffsetPair pair = DateUtils.parseOffset(offsetStr);
        OffsetPair expected = new OffsetPair(false, 2, 30);
        Assertions.assertThat(pair).isEqualTo(expected);
    }

    @Test
    void test_offset_neg_parsing() {
        String offsetStr = "-0600";
        OffsetPair pair = DateUtils.parseOffset(offsetStr);
        OffsetPair expected = new OffsetPair(true, 6, 0);
        Assertions.assertThat(pair).isEqualTo(expected);
    }
}
