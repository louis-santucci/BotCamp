package com.botcamp.botcamp_api.repository.entity;

import com.botcamp.common.entity.AEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

import java.util.Set;

import static com.botcamp.common.entity.EntityNamingAttributes.*;

@EqualsAndHashCode(callSuper = true)
@Entity(name = BOTCAMP_USER)
@Table(name = BOTCAMP_USER)
@Data
@Builder
@AllArgsConstructor
public class BotcampUserEntity extends AEntity {

    protected BotcampUserEntity() {
        super();
    }

    @Column(name = BOTCAMP_USER_USERNAME, unique = true)
    private String username;
    @Column(name = BOTCAMP_USER_PASSWORD)
    @JsonIgnore
    private String password;
    @Column(name = BOTCAMP_USER_AUTHORIZATIONS)
    private String authorizations;
    @OneToMany(mappedBy = "botcampUser")
    private Set<TaskExecutionEntity> executions;
}
