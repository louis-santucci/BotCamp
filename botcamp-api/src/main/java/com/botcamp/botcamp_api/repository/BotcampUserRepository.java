package com.botcamp.botcamp_api.repository;

import com.botcamp.botcamp_api.repository.entity.BotcampUserEntity;
import com.botcamp.common.repository.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BotcampUserRepository extends UserRepository<BotcampUserEntity> {
    BotcampUserEntity findByUsername(String username);
}
