package com.botcamp.botcamp_api.execution.callable.exporter;

import com.botcamp.botcamp_api.config.properties.ExportProperties;
import com.botcamp.botcamp_api.execution.Execution;
import com.botcamp.botcamp_api.service.EmailSender;
import com.botcamp.common.mail.EmailResults;

public class EmailExporter implements CallableExporter {
    private final ExportProperties exportProperties;
    private final EmailSender emailSender;

    public EmailExporter(ExportProperties exportProperties,
                         EmailSender emailSender) {
        this.exportProperties = exportProperties;
        this.emailSender = emailSender;
    }

    @Override
    public void export(Execution execution, EmailResults data) {
    }
}
