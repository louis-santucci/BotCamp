package com.botcamp.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import static java.lang.Math.abs;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {
    private static final String SPACE = " ";
    private static final String UTC = "UTC";
    private static final String TIME_ZONE_PARIS = "Europe/Paris";

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public static LocalDateTime stringToLocalDateTime(String dateStr, DateTimeFormatter formatter) {
        if (formatter.equals(DateUtils.formatter)) return LocalDateTime.parse(dateStr, formatter);
        formatter = formatter.withLocale(Locale.ENGLISH);
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, formatter);
        OffsetPair offset = parseOffset(dateStr.substring(dateStr.length() - 5));
        LocalDateTime updatedLocalDateTime = addOffsetToLocalDateTime(localDateTime, offset);
        Instant instant = updatedLocalDateTime.atZone(ZoneId.of(UTC)).toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of(TIME_ZONE_PARIS));

        return zonedDateTime.toLocalDateTime();
    }

    public static LocalDate stringToLocalDate(String dateStr) {
        return LocalDate.parse(dateStr, dateFormatter);
    }

    public static String dateTimeToString(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }

    private static LocalDateTime addOffsetToLocalDateTime(LocalDateTime localDateTime, OffsetPair pair) {
        int hours = pair.getOffsetHours();
        int minutes = pair.getOffsetMinutes();
        return pair.isNegative()
                ? localDateTime.minusHours(hours).minusMinutes(minutes)
                : localDateTime.plusHours(hours).plusMinutes(minutes);
    }

    public static String cleanDate(String date) {
        String[] splitDate = Arrays.stream(date.split(SPACE)).filter(s -> !s.isEmpty()).toArray(String[]::new);
        if (splitDate.length == 7) splitDate = Arrays.copyOf(splitDate, splitDate.length - 1);
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = Arrays.stream(splitDate).iterator();

        if (it.hasNext()) sb.append(it.next());
        while (it.hasNext()) {
            sb.append(SPACE);
            sb.append(it.next());
        }

        return sb.toString();
    }

    public static Date localDateToDate(LocalDate date) {
        return java.sql.Date.valueOf(date);
    }


    public static OffsetPair parseOffset(String offsetString) {
        String offsetHoursStr = offsetString.substring(0, 3);
        int offsetHour = Integer.parseInt(offsetHoursStr);
        String offsetMinutesStr = offsetString.substring(3, 5);
        int offsetMinutes = Integer.parseInt(offsetMinutesStr);
        return new OffsetPair(offsetHour < 0, abs(offsetHour), abs(offsetMinutes));
    }

    public static LocalDateTime dateToLocalDateTime(Date dateToConvert) {
        Instant instant = dateToConvert.toInstant();
        return LocalDateTime.ofInstant(
                instant, ZoneId.systemDefault());
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return java.util.Date
                .from(localDateTime.atZone(ZoneId.systemDefault())
                        .toInstant());
    }
}
