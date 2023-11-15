package com.botcamp.common.service;

import com.botcamp.common.entity.AEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface JwtUserDetailsService<ENTITY extends AEntity> extends UserDetailsService {
    ENTITY save(Object user);

}
