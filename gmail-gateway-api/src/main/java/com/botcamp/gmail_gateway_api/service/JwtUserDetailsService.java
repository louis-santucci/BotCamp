package com.botcamp.gmail_gateway_api.service;

import com.botcamp.gmail_gateway_api.repository.BotcampUserRepository;
import com.botcamp.gmail_gateway_api.repository.entity.BotcampUserEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service

public class JwtUserDetailsService implements UserDetailsService {

    private BotcampUserRepository userRepository;
    private PasswordEncoder encoder;

    public JwtUserDetailsService(BotcampUserRepository botcampUserRepository,
                                 PasswordEncoder passwordEncoder) {
        this.userRepository = botcampUserRepository;
        this.encoder = passwordEncoder;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BotcampUserEntity entity = userRepository.findByUsername(username);
        if (entity == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new com.botcamp.gmail_gateway_api.config.BotcampUser(entity.getUsername(), entity.getPassword(), new ArrayList<>(), entity.getGmailEmail());
    }

    public BotcampUserEntity save(BotcampUser user) {
        BotcampUserEntity newUser = new BotcampUserEntity();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(newUser);
    }

}
