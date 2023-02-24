package com.project.nhatrotot.rest.advice.CustomException;

import org.springframework.http.HttpStatus;

public class CustomErrorInfo extends ErrorInfo {
    private final HttpStatus status;

    public HttpStatus getStatus() {
        return status;
    }

    public CustomErrorInfo(CustomException ex) {
        super(ex);
        if (ex.getStatus() != null) {
            status = ex.getStatus();
        } else {
            status = HttpStatus.BAD_REQUEST;
        }

    }
}