package com.botcamp.gmail_gateway_api.config;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class GatewayUser extends User {

    private final String gmailEmail;

    public GatewayUser(String username, String password, Collection<? extends GrantedAuthority> authorities, String gmailEmail) {
        super(username, password, authorities);
        this.gmailEmail = gmailEmail;
    }
}
