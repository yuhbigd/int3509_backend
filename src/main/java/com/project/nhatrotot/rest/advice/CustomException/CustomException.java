package com.project.nhatrotot.rest.advice.CustomException;

public abstract class CustomException extends RuntimeException  {

    public CustomException(String message) {
        super(message);
    }

}
