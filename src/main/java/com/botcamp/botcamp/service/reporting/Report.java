package com.botcamp.botcamp.service.reporting;

import java.time.LocalDateTime;
import java.util.Set;

public interface Report {
    Long getId();
    LocalDateTime getCreatedAt();
    Set<Record> getRecords();
}
