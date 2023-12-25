package com.botcamp.botcamp_api.repository.entity;

import com.botcamp.botcamp_api.execution.ExecutionStatus;
import com.botcamp.botcamp_api.execution.ExecutionType;
import com.botcamp.botcamp_api.mailing.QueryParameter;
import com.botcamp.botcamp_api.repository.entity.serializer.QueryParameterConverter;
import com.botcamp.common.entity.AEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

import static com.botcamp.common.entity.EntityNamingAttributes.*;

@Entity(name = EXECUTION)
@Table(name = EXECUTION)
@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TaskExecutionEntity extends AEntity implements Comparable<TaskExecutionEntity> {

    protected TaskExecutionEntity() {
        super();
    }

    @Column(name = EXECUTION_STATUS)
    @Enumerated(EnumType.STRING)
    private ExecutionStatus status;

    @Column(name = EXECUTION_TYPE)
    @Enumerated(EnumType.STRING)
    private ExecutionType type;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private BotcampUserEntity botcampUser;

    @Column(name = EXECUTION_QUERY_PARAMETER, length = 300)
    @Convert(converter = QueryParameterConverter.class)
    private QueryParameter queryParameter;

    @Override
    public int compareTo(TaskExecutionEntity o) {
        return this.getUpdatedAt().compareTo(o.getUpdatedAt());
    }
}
