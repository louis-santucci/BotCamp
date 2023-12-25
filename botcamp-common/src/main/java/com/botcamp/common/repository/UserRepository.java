package com.botcamp.common.repository;

import com.botcamp.common.entity.AEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository<T extends AEntity> extends CrudRepository<T, String> {
    T findByUsername(String username);
}
