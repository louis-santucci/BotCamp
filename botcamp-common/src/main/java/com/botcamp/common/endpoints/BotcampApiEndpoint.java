package com.botcamp.common.endpoints;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BotcampApiEndpoint {
    // Controller names
    public static final String JWT_AUTH_CONTROLLER = "JWT Auth Controller";
    public static final String REPORTING_CONTROLLER = "Reporting Controller";
    public static final String EXECUTION_CONTROLLER = "Execution Controller";

    // Controllers paths
    public static final String API_AUTH = "/api/auth";
    public static final String API_REPORTING = "/api/reporting";
    public static final String API_EXECUTION = "/api/execution";

    // JWT Auth Controller
    public static final String AUTH = "/authenticate";

    // Reporting Controller
    public static final String GENERATE = "/generate";
    public static final String SCHEDULE = "/schedule";
    public static final String FROM_QUERY_PARAM = "from";
    public static final String BEGIN_DATE_QUERY_PARAM = "begin_date";
    public static final String END_DATE_QUERY_PARAM = "end_date";
    public static final String SUBJECT_QUERY_PARAM = "subject";

    // Execution Controller
    public static final String EXECUTION_GET = "/get";
}
