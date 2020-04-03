package com.test.api;

import com.test.MainApiException;

public final class PetsApiException extends MainApiException {
    public PetsApiException(int statusCode, String statusMessage) {
        super(statusCode, statusMessage);
    }


}