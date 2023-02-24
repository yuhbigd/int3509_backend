package com.project.nhatrotot.rest.advice;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.nhatrotot.rest.advice.CustomException.CustomErrorInfo;
import com.project.nhatrotot.rest.advice.CustomException.CustomException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomExcptionHandle {
    @ExceptionHandler(value = { CustomException.class })
    public ResponseEntity<String> customException(CustomException e) {
        ObjectMapper mapper = new ObjectMapper();
        CustomErrorInfo errorInfo = new CustomErrorInfo(e);
        String respJSONstring = "{}";
        try {
            respJSONstring = mapper.writeValueAsString(errorInfo);
        } catch (JsonProcessingException e1) {
            e1.printStackTrace();
        }
        return new ResponseEntity<String>(respJSONstring, errorInfo.getStatus());
    }
}
