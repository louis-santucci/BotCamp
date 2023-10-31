package com.botcamp.gmail_gateway_api.config.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class BandcampUser extends User {

    private String gmailEmail;

    public BandcampUser(String username, String password, Collection<? extends GrantedAuthority> authorities, String gmailEmail) {
        super(username, password, authorities);
        this.gmailEmail = gmailEmail;
    }

    public BandcampUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, String gmailEmail) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.gmailEmail = gmailEmail;
    }
}
