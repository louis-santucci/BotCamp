package com.botcamp.gmail_gateway_api.controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ControllerEndpoint {
    // Controller names
    public static final String JWT_AUTH_CONTROLLER = "JWT Auth Controller";
    public static final String MAIL_CONTROLLER = "Mail Controller";
    // Controller paths
    public static final String API_MAIL = "/api/mail";
    public static final String API_AUTH = "/api/auth";

    // JWT Auth Controller
    public static final String AUTH = "/authenticate";

    // Mail Controller
    public static final String GET_LIST = "/all";

}
