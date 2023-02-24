package com.project.nhatrotot.rest.advice.CustomException;

import org.springframework.http.HttpStatus;

/**
 * GUIDInvalidException
 */
public class GUIDInvalidException extends CustomException {
    public GUIDInvalidException(String message, HttpStatus status) {
        super(message, status);
    }

    public GUIDInvalidException(String message) {
        super(message);
    }
}