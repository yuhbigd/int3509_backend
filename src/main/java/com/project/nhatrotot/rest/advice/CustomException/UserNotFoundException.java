package com.project.nhatrotot.rest.advice.CustomException;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends CustomException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, HttpStatus status) {
        super(message, status);
    }
}
