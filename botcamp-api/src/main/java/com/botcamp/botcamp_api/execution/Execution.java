package com.botcamp.botcamp_api.execution;

import com.botcamp.botcamp_api.config.BotcampUser;
import com.botcamp.common.mail.QueryParameter;
import com.botcamp.botcamp_api.repository.entity.TaskExecutionEntity;
import com.botcamp.common.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Path;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Execution {
    private String id;
    private ExecutionStatus status;
    private ExecutionType type;
    private BotcampUser user;
    private QueryParameter queryParameter;
    private String createdAt;
    private String updatedAt;
    private Path reportPath;
    private boolean emailSent;

    public Execution(TaskExecutionEntity entity) {
        this.id = entity.getId();
        this.queryParameter = entity.getQueryParameter();
        this.status = entity.getStatus();
        this.user = new BotcampUser(entity.getBotcampUser());
        this.type = entity.getType();
        this.createdAt = entity.getCreatedAt() == null ? null : DateUtils.dateTimeToString(DateUtils.dateToLocalDateTime(entity.getCreatedAt()));
        this.updatedAt = entity.getUpdatedAt() == null ? null : DateUtils.dateTimeToString(DateUtils.dateToLocalDateTime(entity.getUpdatedAt()));
        this.reportPath = entity.getReportPath();
        this.emailSent = entity.isEmailSent();
    }


}
