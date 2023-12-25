package com.botcamp.botcamp_api.service;

import com.botcamp.botcamp_api.config.BotcampUser;
import com.botcamp.botcamp_api.execution.Execution;
import com.botcamp.botcamp_api.execution.ExecutionStatus;
import com.botcamp.botcamp_api.execution.ExecutionType;
import com.botcamp.botcamp_api.mailing.QueryParameter;
import com.botcamp.common.request.SortingOrderParameter;

import java.util.List;

public interface TaskExecutionService {
    List<Execution> getExecutions(
            String beginDate,
            String endDate,
            List<ExecutionStatus> statuses,
            List<ExecutionType> types,
            SortingOrderParameter sorting,
            BotcampUser botcampUser,
            boolean userOnly);


    Execution getExecution(String id);

    Execution updateExecution(String id, ExecutionStatus status);

    Execution createExecution(QueryParameter queryParameter,
                              ExecutionType type,
                              BotcampUser botcampUser);
}
