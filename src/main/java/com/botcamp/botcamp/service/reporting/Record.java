package com.botcamp.botcamp.service.reporting;

import java.util.Set;

public interface Record {
    Set<String> getFields();
    String toString();
    String toReportLine();
}
