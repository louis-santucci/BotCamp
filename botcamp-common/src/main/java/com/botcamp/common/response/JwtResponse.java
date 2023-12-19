package com.botcamp.common.response;

import com.botcamp.common.jwt.JwtToken;
import com.botcamp.common.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 68181176765887687L;

    private static final String JWT_TOKEN = "jwt_token";
    private static final String EXPIRES_AT = "expires_at";

    @JsonProperty(JWT_TOKEN)
    private String jwtToken;
    @JsonProperty(EXPIRES_AT)
    private String expiresAt;

    public JwtResponse(JwtToken token) {
        this.jwtToken = token.getToken();
        this.expiresAt = DateUtils.dateTimeToString(token.getExpiresAt());
    }
}
