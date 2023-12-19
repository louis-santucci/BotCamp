package com.botcamp.common.endpoints;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BotcampApiEndpoint {
    // Controller names
    public static final String JWT_AUTH_CONTROLLER = "JWT Auth Controller";
    public static final String REPORTING_CONTROLLER = "Reporting Controller";

    // Controllers paths
    public static final String API_AUTH = "/api/auth";
    public static final String API_REPORTING = "/api/reporting";

    // JWT Auth Controller
    public static final String AUTH = "/authenticate";

    // Reporting Controller
    public static final String GENERATE = "/generate";
    public static final String SCHEDULE = "/schedule";
}
