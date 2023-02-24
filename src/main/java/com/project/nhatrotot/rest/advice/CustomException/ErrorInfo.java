package com.project.nhatrotot.rest.advice.CustomException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorInfo {
    public final String className;
    public final String exMessage;

    public ErrorInfo(Exception ex) {
        ex.printStackTrace();
        this.className = ex.getClass().getSimpleName();
        this.exMessage = ex.getMessage();
    }
}