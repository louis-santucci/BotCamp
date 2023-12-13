package com.botcamp.gmail_gateway_api.service;

import com.botcamp.common.config.properties.SecurityConfigProperties;
import com.botcamp.common.service.JwtUserDetailsService;
import com.botcamp.common.utils.JwtUtils;
import com.botcamp.gmail_gateway_api.repository.GatewayUserRepository;
import com.botcamp.gmail_gateway_api.repository.entity.GatewayUserEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service

public class GatewayUserDetailsService implements JwtUserDetailsService<GatewayUserEntity> {

    private GatewayUserRepository userRepository;
    private SecurityConfigProperties securityConfigProperties;
    private PasswordEncoder encoder;

    public GatewayUserDetailsService(GatewayUserRepository gatewayUserRepository,
                                     SecurityConfigProperties securityConfigProperties,
                                     PasswordEncoder passwordEncoder) {
        this.userRepository = gatewayUserRepository;
        this.securityConfigProperties = securityConfigProperties;
        this.encoder = passwordEncoder;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        GatewayUserEntity entity = userRepository.findByUsername(username);
        if (entity == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new com.botcamp.gmail_gateway_api.config.GatewayUser(entity.getUsername(), entity.getPassword(), new ArrayList<>(), entity.getGmailEmail());
    }

    @Override
    public GatewayUserEntity save(Object user) {
        GatewayUser gatewayUser = (GatewayUser) user;
        GatewayUserEntity newUser = GatewayUserEntity.builder()
                .username(gatewayUser.getUsername())
                .password(gatewayUser.getPassword())
                .gmailEmail(gatewayUser.getGmailEmail())
                .build();
        return userRepository.save(newUser);
    }

    @Override
    public SecurityConfigProperties getSecurityConfigProperties() {
        return securityConfigProperties;
    }
}
