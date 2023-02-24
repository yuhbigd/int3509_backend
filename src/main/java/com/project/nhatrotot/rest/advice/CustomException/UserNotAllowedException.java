package com.project.nhatrotot.rest.advice.CustomException;

import org.springframework.http.HttpStatus;

public class UserNotAllowedException extends CustomException {
    public UserNotAllowedException(String message) {
        super(message);
    }

    public UserNotAllowedException(String message, HttpStatus status) {
        super(message, status);
    }

}