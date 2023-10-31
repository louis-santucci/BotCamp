package com.botcamp.gmail_gateway_api.repository;

import com.botcamp.gmail_gateway_api.repository.entity.BandcampUserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BandcampUserRepository extends CrudRepository<BandcampUserEntity, Integer> {
}
