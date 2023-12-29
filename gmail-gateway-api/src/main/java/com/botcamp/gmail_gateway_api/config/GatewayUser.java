package com.botcamp.gmail_gateway_api.config;

import com.botcamp.gmail_gateway_api.repository.entity.GatewayUserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Arrays;
import java.util.Collection;

public class GatewayUser extends User {
    private static final String COMMA = ",";

    private String gmailEmail;

    public GatewayUser(String username, String password, Collection<? extends GrantedAuthority> authorities, String gmailEmail) {
        super(username, password, authorities);
        this.gmailEmail = gmailEmail;
    }

    public GatewayUser(GatewayUserEntity entity) {
        super(
                entity.getUsername(),
                entity.getPassword(),
                Arrays.stream(entity.getAuthorizations().split(COMMA))
                        .map(SimpleGrantedAuthority::new)
                        .toList());
        this.gmailEmail = entity.getGmailEmail();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GatewayUser{");
        sb.append("Username=").append(getUsername()).append(", ");
        sb.append("GmailEmail=").append(getGmailEmail()).append(", ");
        sb.append("Authorities=").append(getAuthorities());
        sb.append("}");
        return sb.toString();
    }

    @Override
    @JsonIgnore
    public Collection<GrantedAuthority> getAuthorities() {
        return super.getAuthorities();
    }

    public String getGmailEmail() {
        return gmailEmail;
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
