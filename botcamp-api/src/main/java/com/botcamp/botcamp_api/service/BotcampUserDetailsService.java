package com.botcamp.botcamp_api.service;

import com.botcamp.botcamp_api.repository.BotcampUserRepository;
import com.botcamp.botcamp_api.repository.entity.BotcampUserEntity;
import com.botcamp.common.service.JwtUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BotcampUserDetailsService implements JwtUserDetailsService<BotcampUserEntity> {

    private BotcampUserRepository userRepository;
    private PasswordEncoder encoder;

    public BotcampUserDetailsService(BotcampUserRepository botcampUserRepository,
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
        return new com.botcamp.botcamp_api.config.BotcampUser(entity.getUsername(), entity.getPassword(), new ArrayList<>(0));
    }

    @Override
    public BotcampUserEntity save(Object user) {
        BotcampUser botcampUser = (BotcampUser) user;
        BotcampUserEntity newUser = new BotcampUserEntity();
        newUser.setUsername(botcampUser.getUsername());
        newUser.setPassword(encoder.encode(botcampUser.getPassword()));
        return userRepository.save(newUser);
    }

}
