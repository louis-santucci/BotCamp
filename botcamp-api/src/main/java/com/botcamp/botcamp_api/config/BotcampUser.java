package com.botcamp.botcamp_api.config;

import com.botcamp.botcamp_api.repository.entity.BotcampUserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Arrays;
import java.util.Collection;

public class BotcampUser extends User {
    private static final String COMMA = ",";

    public BotcampUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public BotcampUser(BotcampUserEntity entity) {
        super(
                entity.getUsername(),
                entity.getPassword(),
                Arrays.stream(entity.getAuthorizations().split(COMMA))
                        .map(SimpleGrantedAuthority::new)
                        .toList());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BotcampUser{");
        sb.append("Username=").append(getUsername()).append(", ");
        sb.append("Authorities=").append(getAuthorities());
        sb.append("}");
        return sb.toString();
    }

    @Override
    @JsonIgnore
    public Collection<GrantedAuthority> getAuthorities() {
        return super.getAuthorities();
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return super.getPassword();
    }


    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return super.isAccountNonExpired();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return super.isAccountNonLocked();
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return super.isCredentialsNonExpired();
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return super.isEnabled();
    }
}
