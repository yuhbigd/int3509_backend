package com.project.nhatrotot.rest.advice.CustomException;

import org.springframework.http.HttpStatus;

public class GeneralException extends CustomException {
    public GeneralException(String message, HttpStatus status) {
        super(message, status);
    }

    public GeneralException(String message) {
        super(message);
    }
}