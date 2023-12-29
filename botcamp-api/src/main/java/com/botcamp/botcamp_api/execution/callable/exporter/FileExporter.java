package com.botcamp.botcamp_api.execution.callable.exporter;

import com.botcamp.botcamp_api.config.properties.ExportProperties;
import com.botcamp.botcamp_api.execution.Execution;
import com.botcamp.botcamp_api.service.TaskExecutionService;
import com.botcamp.common.mail.Email;
import com.botcamp.common.mail.EmailError;
import com.botcamp.common.mail.EmailResults;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

public class FileExporter implements CallableExporter {
    private static final char NEWLINE = '\n';

    private final TaskExecutionService executionService;
    private final Path folderPath;

    public FileExporter(ExportProperties exportProperties,
                        TaskExecutionService executionService) throws IOException {
        this.folderPath = Files.createDirectories(Paths.get(exportProperties.getPath()));
        this.executionService = executionService;
    }

    @Override
    public void export(Execution execution, EmailResults data) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("---------[EMAILS]---------").append(NEWLINE);
        List<Email> emails = data.getEmails();
        List<EmailError> emailErrors = data.getErrors();
        Iterator<Email> iterator = emails.iterator();
        while (iterator.hasNext()) {
            Email email = iterator.next();
            sb.append("Sender: ").append(email.getSender()).append(NEWLINE);
            sb.append("Subject: ").append(email.getSubject()).append(NEWLINE);
            sb.append("DateTime: ").append(email.getDateTime()).append(NEWLINE);
            sb.append("Body Type: ").append(email.getBodyType()).append(NEWLINE);
            sb.append("Body:").append(NEWLINE).append(email.getBody()).append(NEWLINE);
            if (iterator.hasNext()) sb.append("--------------------------").append(NEWLINE);
        }
        sb.append("---------[ERRORS]---------").append(NEWLINE);
        for (EmailError emailError : emailErrors) {
            sb.append("Id: ").append(emailError.getId()).append(NEWLINE);
            sb.append("Error Message: ").append(emailError.getErrorMessage()).append(NEWLINE);
            sb.append("--------------------------").append(NEWLINE);
        }
        byte[] bytes = sb.toString().getBytes();
        String filename = generateRandomFilename() + ".txt";
        Path newFilePath = Paths.get(folderPath.toString(), filename);
        Path path = Files.write(newFilePath, bytes);
        this.executionService.updateExecutionReportPath(execution.getId(), path);
    }

    private String generateRandomFilename() {
        return RandomStringUtils.random(10, true, true);
    }
}
