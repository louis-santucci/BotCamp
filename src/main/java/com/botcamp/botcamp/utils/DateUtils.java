package com.botcamp.botcamp.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {

    private static final char COMMA = ',';
    private static final char PLUS = '+';
    private static final String UTC = "UTC";
    private static final String TIME_ZONE_PARIS = "Europe/Paris";


    public static LocalDateTime StringToDateTime(String dateStr, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format).withLocale(Locale.ENGLISH);
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, formatter);
        Instant instant = localDateTime.atZone(ZoneId.of(UTC)).toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of(TIME_ZONE_PARIS));

        return zonedDateTime.toLocalDateTime();
    }

    public static String cleanDate(String date) {
        return date.substring(date.indexOf(COMMA) + 1, date.indexOf(PLUS)).trim();
    }
}
