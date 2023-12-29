package com.botcamp.botcamp_api.service;

import com.botcamp.botcamp_api.config.BotcampUser;
import com.botcamp.botcamp_api.repository.BotcampUserRepository;
import com.botcamp.botcamp_api.repository.entity.BotcampUserEntity;
import com.botcamp.common.config.properties.SecurityConfigProperties;
import com.botcamp.common.service.JwtUserDetailsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class BotcampUserDetailsService implements JwtUserDetailsService<BotcampUserEntity> {

    private static final String COMMA = ",";

    private BotcampUserRepository userRepository;
    private PasswordEncoder encoder;
    private SecurityConfigProperties securityConfigProperties;

    public BotcampUserDetailsService(BotcampUserRepository botcampUserRepository,
                                     SecurityConfigProperties securityConfigProperties,
                                     PasswordEncoder passwordEncoder) {
        this.userRepository = botcampUserRepository;
        this.securityConfigProperties = securityConfigProperties;
        this.encoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BotcampUserEntity entity = userRepository.findByUsername(username);
        if (entity == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        List<SimpleGrantedAuthority> authorities = Arrays.stream(entity.getAuthorizations().split(COMMA)).map(SimpleGrantedAuthority::new).toList();
        return new com.botcamp.botcamp_api.config.BotcampUser(entity.getUsername(), entity.getPassword(), authorities);
    }

    @Override
    public BotcampUserEntity save(Object user) {
        BotcampUser botcampUser = (BotcampUser) user;
        List<String> authString = botcampUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        String authorities = StringUtils.join(authString, COMMA);
        BotcampUserEntity newUser = BotcampUserEntity.builder()
                .username(botcampUser.getUsername())
                .password(botcampUser.getPassword())
                .authorizations(authorities)
                .build();
        return userRepository.save(newUser);
    }

    @Override
    public SecurityConfigProperties getSecurityConfigProperties() {
        return securityConfigProperties;
    }

}
