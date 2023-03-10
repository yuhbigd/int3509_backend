package com.project.nhatrotot.rest.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.nhatrotot.rest.advice.CustomException.CustomException;
import com.project.nhatrotot.rest.advice.CustomException.ErrorInfo;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class ExceptionControllerAdvice {

    @ExceptionHandler(value = { Exception.class, CustomException.class })
    public ResponseEntity<String> exception(Exception e) {
        ObjectMapper mapper = new ObjectMapper();
        ErrorInfo errorInfo = new ErrorInfo(e);
        System.out.println(errorInfo);
        String respJSONstring = "{}";
        try {
            respJSONstring = mapper.writeValueAsString(errorInfo);
        } catch (JsonProcessingException e1) {
            e1.printStackTrace();
        }
        return ResponseEntity.badRequest().body(respJSONstring);
    }

}
