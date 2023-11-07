package com.botcamp.gmail_gateway_api.controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ControllerEndpoint {
    // Controllers paths
    public static final String V1_MAIL = "/api/v1/mail";
    public static final String V1_AUTH = "/api/v1/auth";

    // JWT Auth Controller
    public static final String AUTH = "/authenticate";

    // Mail Controller
    public static final String GET_LIST = "/all";

}
