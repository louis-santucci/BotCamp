package com.botcamp.common.service;

import org.springframework.stereotype.Component;

@Component
public class TokenExtractor {
    private String jwtToken;

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
