package com.botcamp.gmail_gateway_api.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JwtRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 655876186556145617L;

    private String username;
    private String password;
}
