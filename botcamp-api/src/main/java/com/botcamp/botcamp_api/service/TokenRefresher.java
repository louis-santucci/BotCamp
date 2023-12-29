package com.botcamp.botcamp_api.service;

import com.botcamp.common.jwt.JwtToken;

import java.io.IOException;

public interface TokenRefresher {
    JwtToken provideJwtToken() throws IOException;
}
