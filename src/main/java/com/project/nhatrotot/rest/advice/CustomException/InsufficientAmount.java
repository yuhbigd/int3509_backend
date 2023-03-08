package com.project.nhatrotot.rest.advice.CustomException;

import org.springframework.http.HttpStatus;

public class InsufficientAmount extends CustomException {
    public InsufficientAmount(String message) {
        super(message);
    }

    public InsufficientAmount(String message, HttpStatus status) {
        super(message, status);
    }

}