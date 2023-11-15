package com.botcamp.gmail_gateway_api.repository;

import com.botcamp.common.repository.UserRepository;
import com.botcamp.gmail_gateway_api.repository.entity.GatewayUserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GatewayUserRepository extends UserRepository<GatewayUserEntity> {
}
