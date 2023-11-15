package com.botcamp.botcamp_api.controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ControllerEndpoint {
    // Controllers paths
    public static final String V1_AUTH = "/api/v1/auth";

    // JWT Auth Controller
    public static final String AUTH = "/authenticate";
}
