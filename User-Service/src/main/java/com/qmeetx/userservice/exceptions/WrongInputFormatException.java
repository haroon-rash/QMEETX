package com.qmeetx.userservice.exceptions;

public class WrongInputFormatException extends IllegalArgumentException {
    public WrongInputFormatException(String message) {
        super(message);
    }
}
