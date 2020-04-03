package com.test;

import io.vertx.serviceproxy.ServiceException;

public class MainApiException extends ServiceException {

    public static final MainApiException INTERNAL_SERVER_ERROR = new MainApiException(500, "Internal Server Error");
    private final int statusCode;
    private final String statusMessage;

    public MainApiException(int statusCode, String statusMessage) {
        super(statusCode, statusMessage);
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}