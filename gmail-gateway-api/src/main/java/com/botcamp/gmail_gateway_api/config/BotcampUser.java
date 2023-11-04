package com.botcamp.gmail_gateway_api.config;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class BotcampUser extends User {

    private String gmailEmail;

    public BotcampUser(String username, String password, Collection<? extends GrantedAuthority> authorities, String gmailEmail) {
        super(username, password, authorities);
        this.gmailEmail = gmailEmail;
    }

    public BotcampUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, String gmailEmail) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.gmailEmail = gmailEmail;
    }
}
