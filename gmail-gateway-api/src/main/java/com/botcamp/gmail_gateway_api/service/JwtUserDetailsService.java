package com.botcamp.gmail_gateway_api.service;

import com.botcamp.gmail_gateway_api.repository.GatewayUserRepository;
import com.botcamp.gmail_gateway_api.repository.entity.GatewayUserEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service

public class JwtUserDetailsService implements UserDetailsService {

    private GatewayUserRepository userRepository;
    private PasswordEncoder encoder;

    public JwtUserDetailsService(GatewayUserRepository gatewayUserRepository,
                                 PasswordEncoder passwordEncoder) {
        this.userRepository = gatewayUserRepository;
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

    public GatewayUserEntity save(GatewayUser user) {
        GatewayUserEntity newUser = new GatewayUserEntity();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(newUser);
    }

}
