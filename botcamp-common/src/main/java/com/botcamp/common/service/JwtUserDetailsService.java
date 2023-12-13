package com.botcamp.common.service;

import com.botcamp.common.config.properties.SecurityConfigProperties;
import com.botcamp.common.entity.AEntity;
import com.botcamp.common.utils.JwtUtils;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface JwtUserDetailsService<ENTITY extends AEntity> extends UserDetailsService {
    ENTITY save(Object user);
    SecurityConfigProperties getSecurityConfigProperties();
    default String getUsernameFromToken(String token) {
        String secret = getSecurityConfigProperties().getJwt().getSecret();
        return JwtUtils.getUsernameFromToken(token, secret);
    }
}
