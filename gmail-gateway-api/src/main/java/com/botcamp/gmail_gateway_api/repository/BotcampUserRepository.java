package com.botcamp.gmail_gateway_api.repository;

import com.botcamp.gmail_gateway_api.repository.entity.BotcampUserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BotcampUserRepository extends CrudRepository<BotcampUserEntity, Integer> {
    BotcampUserEntity findByUsername(String username);
}
