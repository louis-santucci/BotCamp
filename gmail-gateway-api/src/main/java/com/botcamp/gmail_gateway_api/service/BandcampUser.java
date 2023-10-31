package com.botcamp.gmail_gateway_api.service;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BandcampUser {

    private String username;

    private String password;

    private String gmailEmail;
}
