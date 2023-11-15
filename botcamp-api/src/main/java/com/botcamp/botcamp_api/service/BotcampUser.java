package com.botcamp.botcamp_api.service;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BotcampUser {
    private String username;

    private String password;
}
