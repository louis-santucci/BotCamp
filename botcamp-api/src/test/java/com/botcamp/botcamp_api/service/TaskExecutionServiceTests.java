package com.botcamp.botcamp_api.service;

import com.botcamp.botcamp_api.config.BotcampUser;
import com.botcamp.botcamp_api.execution.Execution;
import com.botcamp.botcamp_api.execution.ExecutionStatus;
import com.botcamp.botcamp_api.execution.ExecutionType;
import com.botcamp.botcamp_api.mailing.QueryParameter;
import com.botcamp.botcamp_api.repository.BotcampUserRepository;
import com.botcamp.botcamp_api.repository.TaskExecutionRepository;
import com.botcamp.botcamp_api.repository.entity.BotcampUserEntity;
import com.botcamp.botcamp_api.repository.entity.TaskExecutionEntity;
import com.botcamp.botcamp_api.service.impl.TaskExecutionServiceImpl;
import com.botcamp.common.request.SortingOrderParameter;
import com.botcamp.common.utils.DateUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class TaskExecutionServiceTests {
    private static final String USERNAME = "toto@toto.fr";
    private static final String OTHER_USERNAME = "tutu@tutu.fr";
    private static final String PASSWORD = "test";
    private static final String AUTH = "ADMIN";
    private TaskExecutionService taskExecutionService;
    private TaskExecutionRepository taskExecutionRepository;
    private BotcampUserRepository botcampUserRepository;

    private final BotcampUser botcampUser = new BotcampUser(USERNAME, PASSWORD, new ArrayList<>());
    private final BotcampUser botcampUser2 = new BotcampUser(OTHER_USERNAME, PASSWORD, new ArrayList<>());
    private final BotcampUserEntity botcampUserEntity = new BotcampUserEntity(USERNAME, PASSWORD, AUTH, new HashSet<>());
    private final BotcampUserEntity botcampUserEntity2 = new BotcampUserEntity(OTHER_USERNAME, PASSWORD, AUTH, new HashSet<>());

    void initMocks(Stream<TaskExecutionEntity> stream) {
        taskExecutionRepository = Mockito.mock(TaskExecutionRepository.class);
        botcampUserRepository = mock(BotcampUserRepository.class);
        doReturn(botcampUserEntity).when(botcampUserRepository).findByUsername(USERNAME);
        doReturn(botcampUserEntity2).when(botcampUserRepository).findByUsername(OTHER_USERNAME);
        doReturn(stream).when(taskExecutionRepository).findAllExecutions();
        doReturn(stream).when(taskExecutionRepository).findAllByBotcampUser(botcampUserEntity2);
        taskExecutionService = new TaskExecutionServiceImpl(taskExecutionRepository, botcampUserRepository);
    }

    Stream<TaskExecutionEntity> initStream() {
        var e1 = buildExecutionEntity(ExecutionStatus.FAILURE, ExecutionType.GMAIL, botcampUserEntity, "AAAA", "2020-01-01 00:00:00.000");
        var e2 = buildExecutionEntity(ExecutionStatus.SUCCESS, ExecutionType.OUTLOOK, botcampUserEntity, "BBBB", "2020-01-01 00:00:00.000");
        var e3 = buildExecutionEntity(ExecutionStatus.SUCCESS, ExecutionType.GMAIL, botcampUserEntity, "CCCC", "2018-01-01 00:00:00.000");
        var e4 = buildExecutionEntity(ExecutionStatus.SUCCESS, ExecutionType.GMAIL, botcampUserEntity, "DDDD", "2024-01-01 00:00:00.000");

        return Stream.of(e1, e2, e3, e4);
    }

    Stream<TaskExecutionEntity> initOtherUserStream() {
        var e5 = buildExecutionEntity(ExecutionStatus.SUCCESS, ExecutionType.GMAIL, botcampUserEntity2, "EEEE", "2020-01-01 00:00:00.000");
        return Stream.of(e5);
    }

    Stream<TaskExecutionEntity> initSortStream() {
        var e6 = buildExecutionEntity(ExecutionStatus.SUCCESS, ExecutionType.GMAIL, botcampUserEntity, "FFFF", "2022-01-01 00:00:00.000");
        var e7 = buildExecutionEntity(ExecutionStatus.SUCCESS, ExecutionType.GMAIL, botcampUserEntity, "GGGG", "2020-01-01 00:00:00.000");
        var e8 = buildExecutionEntity(ExecutionStatus.SUCCESS, ExecutionType.GMAIL, botcampUserEntity, "HHHH", "2021-01-01 00:00:00.000");

        return Stream.of(e6, e7, e8);
    }


    private TaskExecutionEntity buildExecutionEntity(ExecutionStatus status, ExecutionType type, BotcampUserEntity entity, String id, String date) {
        TaskExecutionEntity e1 = new TaskExecutionEntity(status, type, entity, new QueryParameter());
        e1.setId(id);
        e1.setUpdatedAt(DateUtils.localDateTimeToDate(DateUtils.stringToLocalDateTime(date, DateUtils.formatter)));
        e1.setCreatedAt(DateUtils.localDateTimeToDate(DateUtils.stringToLocalDateTime(date, DateUtils.formatter)));

        return e1;
    }

    @Test
    void testStatus() {
        initMocks(initStream());
        List<Execution> res = this.taskExecutionService.getExecutions(null, null, List.of(ExecutionStatus.FAILURE), null, null, botcampUser, false);

        assertThat(res.size()).isEqualTo(1);
        Execution result = res.get(0);
        assertThat(result.getStatus()).isEqualTo(ExecutionStatus.FAILURE);
        assertThat(result.getId()).isEqualTo("AAAA");
    }

    @Test
    void testType() {
        initMocks(initStream());
        List<Execution> res = this.taskExecutionService.getExecutions(null, null, null, List.of(ExecutionType.OUTLOOK), null, botcampUser, false);

        assertThat(res.size()).isEqualTo(1);
        Execution result = res.get(0);
        assertThat(result.getType()).isEqualTo(ExecutionType.OUTLOOK);
        assertThat(result.getId()).isEqualTo("BBBB");
    }

    @Test
    void testBeginDate() {
        initMocks(initStream());
        List<Execution> res = this.taskExecutionService.getExecutions("2023-01-01 00:00:00.000", null, null, null, null, botcampUser, false);

        assertThat(res.size()).isEqualTo(1);
        Execution result = res.get(0);
        assertThat(result.getId()).isEqualTo("DDDD");
    }

    @Test
    void testEndDate() {
        initMocks(initStream());
        List<Execution> res = this.taskExecutionService.getExecutions(null, "2019-01-01 00:00:00.000", null, null, null, botcampUser, false);

        assertThat(res.size()).isEqualTo(1);
        Execution result = res.get(0);
        assertThat(result.getId()).isEqualTo("CCCC");
    }

    @Test
    void testUser2() {
        initMocks(initOtherUserStream());
        List<Execution> res = this.taskExecutionService.getExecutions(null, null, null, null, null, botcampUser2, true);

        assertThat(res.size()).isEqualTo(1);
        Execution result = res.get(0);
        assertThat(result.getId()).isEqualTo("EEEE");
    }

    @Test
    void testSort() {
        initMocks(initSortStream());

        List<Execution> res = this.taskExecutionService.getExecutions(null, null, null, null, SortingOrderParameter.ASCENDING, botcampUser, false);

        assertThat(res.size()).isEqualTo(3);
        Execution result1 = res.get(0);
        Execution result2 = res.get(1);
        Execution result3 = res.get(2);
        assertThat(result1.getId()).isEqualTo("GGGG");
        assertThat(result2.getId()).isEqualTo("HHHH");
        assertThat(result3.getId()).isEqualTo("FFFF");
    }

    @Test
    void testUnsort() {
        initMocks(initSortStream());

        List<Execution> res = this.taskExecutionService.getExecutions(null, null, null, null, SortingOrderParameter.DESCENDING, botcampUser, false);

        assertThat(res.size()).isEqualTo(3);
        Execution result1 = res.get(0);
        Execution result2 = res.get(1);
        Execution result3 = res.get(2);
        assertThat(result1.getId()).isEqualTo("FFFF");
        assertThat(result2.getId()).isEqualTo("HHHH");
        assertThat(result3.getId()).isEqualTo("GGGG");

    }

}
