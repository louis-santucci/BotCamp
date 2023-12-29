package com.botcamp.botcamp_api.execution.callable.exporter;

import com.botcamp.botcamp_api.config.properties.ExportProperties;
import com.botcamp.botcamp_api.execution.Execution;
import com.botcamp.botcamp_api.service.TaskExecutionService;
import com.botcamp.botcamp_api.service.impl.TaskExecutionServiceImpl;
import com.botcamp.common.mail.Email;
import com.botcamp.common.mail.EmailResults;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FileExporterTests {
    @MockBean
    TaskExecutionService taskExecutionService;
    private static final String FOLDER = "target/data/outputFile";
    private final ExportProperties exportProperties = new ExportProperties();
    private Execution execution;
    private final Email email = Email.builder()
            .sender("sender")
            .subject("subject")
            .build();
    void init() {
        exportProperties.setPath(FOLDER);
        this.execution = new Execution();
        execution.setId("aaaaa");
    }

    @Test
    void should_persist_file_containing_two_mails() throws IOException {
        init();
        FileExporter exporter = new FileExporter(exportProperties, taskExecutionService);
        exporter.export(execution, new EmailResults(List.of(email), new ArrayList<>(0)));
        Path exportPropertiesPath = Path.of(exportProperties.getPath());

        assertThat(exportPropertiesPath).isNotEmptyDirectory();

        FileUtils.deleteDirectory(new File(FOLDER));
    }
}
