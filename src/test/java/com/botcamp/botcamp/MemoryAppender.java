package com.botcamp.botcamp;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

import java.util.List;

public class MemoryAppender extends ListAppender<ILoggingEvent> {
    public void reset() {
        this.list.clear();
    }

    public long countOccurences(String string, Level lvl) {
        return this.list.stream().filter(iLoggingEvent -> iLoggingEvent.toString().contains(string)
                && iLoggingEvent.getLevel().equals(lvl))
                .count();
    }

    public List<ILoggingEvent> getEventsForLogger(String loggerName) {
        return this.list.stream().filter(iLoggingEvent -> iLoggingEvent.getLoggerName().equals(loggerName)).toList();
    }
}
