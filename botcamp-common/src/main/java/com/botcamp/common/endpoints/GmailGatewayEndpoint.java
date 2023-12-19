package com.botcamp.common.endpoints;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GmailGatewayEndpoint {
    // Controller names
    public static final String JWT_AUTH_CONTROLLER = "JWT Auth Controller";
    public static final String MAIL_CONTROLLER = "Mail Controller";
    public static final String GMAIL_CREDENTIALS_CONTROLLER = "Gmail Credentials Controller";
    // Controller paths
    public static final String API_MAIL = "/api/mail";
    public static final String API_AUTH = "/api/auth";
    public static final String API_CREDENTIALS = "/api/credentials";

    // JWT Auth Controller
    public static final String AUTH = "/authenticate";

    // Mail Controller
    public static final String GET_LIST = "/all";

    // Gmail Credentials Controller
    public static final String GET_CREDENTIALS = "/all";
    public static final String CREATE_CREDENTIALS = "/create";
    public static final String CLEAR_CREDENTIALS = "/clear";

}
