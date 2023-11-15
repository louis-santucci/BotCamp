package com.botcamp.common.repository;

import com.botcamp.common.entity.AEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository<ENTITY extends AEntity> extends CrudRepository<ENTITY, Integer> {
    ENTITY findByUsername(String username);
}
