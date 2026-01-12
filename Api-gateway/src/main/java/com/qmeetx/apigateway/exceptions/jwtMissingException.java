package com.qmeetx.apigateway.exceptions;

public class jwtMissingException extends RuntimeException {
    public jwtMissingException(String message) {
        super(message);
    }
}
