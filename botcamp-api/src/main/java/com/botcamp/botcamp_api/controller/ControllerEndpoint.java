package com.botcamp.botcamp_api.controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ControllerEndpoint {
    // Controllers paths
    public static final String V1_AUTH = "/api/v1/auth";
    public static final String V1_REPORTING = "/api/v1/reporting";

    // JWT Auth Controller
    public static final String AUTH = "/authenticate";

    // Reporting Controller
    public static final String GENERATE = "/generate";
    public static final String SCHEDULE = "/schedule";
}
