package com.botcamp.botcamp_api.execution.callable.exporter;

import com.botcamp.botcamp_api.execution.Execution;
import com.botcamp.common.mail.EmailResults;

import java.io.IOException;

public interface CallableExporter {
    void export(Execution execution, EmailResults data) throws IOException;
}
