package com.project.nhatrotot.rest.advice.CustomException;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class CustomException extends RuntimeException {
    private HttpStatus status;

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
