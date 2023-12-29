package com.botcamp.botcamp_api.repository;

import com.botcamp.botcamp_api.repository.entity.BotcampUserEntity;
import com.botcamp.botcamp_api.repository.entity.TaskExecutionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface TaskExecutionRepository extends CrudRepository<TaskExecutionEntity, String> {
    Stream<TaskExecutionEntity> findAllByBotcampUser(BotcampUserEntity entity);

    @Query(value = "SELECT * from EXECUTION", nativeQuery = true)
    Stream<TaskExecutionEntity> findAllExecutions();
    Optional<TaskExecutionEntity> findExecutionById(String id);
}
