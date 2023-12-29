package com.botcamp.common.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntityNamingAttributes {

    // Gateway User
    public static final String GATEWAY_USER = "gateway_user";
    public static final String GATEWAY_USER_USERNAME = "username";
    public static final String GATEWAY_USER_PASSWORD = "password";
    public static final String GATEWAY_USER_AUTHORIZATIONS = "authorizations";
    public static final String GATEWAY_USER_GMAIL_EMAIL = "gmail_email";

    // Botcamp User
    public static final String BOTCAMP_USER = "botcamp_user";
    public static final String BOTCAMP_USER_USERNAME = "username";
    public static final String BOTCAMP_USER_PASSWORD = "password";
    public static final String BOTCAMP_USER_AUTHORIZATIONS = "authorizations";

    // Execution
    public static final String EXECUTION = "execution";
    public static final String EXECUTION_TYPE = "execution_type";
    public static final String EXECUTION_STATUS = "status";
    public static final String EXECUTION_QUERY_PARAMETER = "query_parameter";
    public static final String EXECUTION_REPORT_PATH = "report_path";
    public static final String EXECUTION_EMAIL_SENT = "email_sent";

    // Job
    public static final String JOB = "job";
    public static final String NAME = "name";
    public static final String ENABLED = "enabled";
    public static final String CRON = "cron";
    public static final String SUBJECT = "cron";
    public static final String JOB_ID = "job_id";
    public static final String RECIPIENT_LIST = "recipient_list";
    public static final String RECIPIENT = "recipient";
}
