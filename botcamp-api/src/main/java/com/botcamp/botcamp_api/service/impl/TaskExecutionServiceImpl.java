package com.botcamp.botcamp_api.service.impl;

import com.botcamp.botcamp_api.config.BotcampUser;
import com.botcamp.botcamp_api.execution.Execution;
import com.botcamp.botcamp_api.execution.ExecutionQueryFilter;
import com.botcamp.botcamp_api.execution.ExecutionStatus;
import com.botcamp.botcamp_api.execution.ExecutionType;
import com.botcamp.common.mail.QueryParameter;
import com.botcamp.botcamp_api.repository.BotcampUserRepository;
import com.botcamp.botcamp_api.repository.TaskExecutionRepository;
import com.botcamp.botcamp_api.repository.entity.BotcampUserEntity;
import com.botcamp.botcamp_api.repository.entity.TaskExecutionEntity;
import com.botcamp.botcamp_api.service.TaskExecutionService;
import com.botcamp.common.request.SortingOrderParameter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class TaskExecutionServiceImpl implements TaskExecutionService {

    private final TaskExecutionRepository taskExecutionRepository;
    private final BotcampUserRepository botcampUserRepository;

    public TaskExecutionServiceImpl(TaskExecutionRepository taskExecutionRepository,
                                    BotcampUserRepository botcampUserRepository) {
        this.taskExecutionRepository = taskExecutionRepository;
        this.botcampUserRepository = botcampUserRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Execution> getExecutions(String beginDateStr,
                                         String endDateStr,
                                         List<ExecutionStatus> statuses,
                                         List<ExecutionType> types,
                                         SortingOrderParameter sorting,
                                         BotcampUser botcampUser,
                                         boolean userOnly) {
        ExecutionQueryFilter executionFilter = new ExecutionQueryFilter(beginDateStr, endDateStr, statuses, types, sorting);
        if (userOnly) {
            BotcampUserEntity botcampUserEntity = botcampUserRepository.findByUsername(botcampUser.getUsername());
            try (Stream<TaskExecutionEntity> executionStream = taskExecutionRepository.findAllByBotcampUser(botcampUserEntity)) {
                Stream<TaskExecutionEntity> filteredStream = executionFilter.filter(executionStream);
                return filteredStream.map(Execution::new).toList();
            }
        }
        try (Stream<TaskExecutionEntity> executionStream = taskExecutionRepository.findAllExecutions()) {
            Stream<TaskExecutionEntity> filteredStream = executionFilter.filter(executionStream);
            return filteredStream.map(Execution::new).toList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Execution getExecution(String id) {
        Optional<TaskExecutionEntity> optional = taskExecutionRepository.findExecutionById(id);
        return optional.map(Execution::new).orElse(null);
    }

    @Override
    @Transactional
    public Execution updateExecution(String id, ExecutionStatus status) {
        Optional<TaskExecutionEntity> optional = taskExecutionRepository.findExecutionById(id);

        if (optional.isPresent()) {
            TaskExecutionEntity executionEntity = optional.get();
            executionEntity.setStatus(status);
            TaskExecutionEntity newEntity = taskExecutionRepository.save(executionEntity);
            return new Execution(newEntity);
        }

        throw new EntityNotFoundException("Execution not found");
    }

    @Override
    @Transactional
    public Execution createExecution(QueryParameter queryParameter, ExecutionType type, BotcampUser botcampUser) {
        BotcampUserEntity botcampUserEntity = botcampUserRepository.findByUsername(botcampUser.getUsername());
        if (botcampUserEntity == null) {
            throw new UsernameNotFoundException("User not found with username: " + botcampUser.getUsername());
        }
        TaskExecutionEntity executionEntity = TaskExecutionEntity
                .builder()
                .queryParameter(queryParameter)
                .botcampUser(botcampUserEntity)
                .type(type)
                .status(ExecutionStatus.PENDING)
                .build();

        TaskExecutionEntity newEntity = taskExecutionRepository.save(executionEntity);

        return new Execution(newEntity);
    }
}
