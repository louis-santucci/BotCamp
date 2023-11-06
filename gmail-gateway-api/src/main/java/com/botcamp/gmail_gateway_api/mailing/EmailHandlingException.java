package com.botcamp.gmail_gateway_api.mailing;

public class EmailHandlingException extends Exception {
    public EmailHandlingException(Exception e) {
        super(e);
    }
}
