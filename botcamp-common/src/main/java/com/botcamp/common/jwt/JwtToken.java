package com.botcamp.common.jwt;

import com.botcamp.common.response.JwtResponse;
import com.botcamp.common.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class JwtToken {
    private String token;
    private LocalDateTime expiresAt;

    public JwtToken(JwtResponse jwtResponse) {
        this.token = jwtResponse.getJwtToken();
        this.expiresAt = DateUtils.stringToLocalDateTime(jwtResponse.getExpiresAt(), DateUtils.formatter);
    }
}
