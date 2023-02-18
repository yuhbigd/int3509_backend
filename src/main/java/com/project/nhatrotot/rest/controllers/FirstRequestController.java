package com.project.nhatrotot.rest.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import com.project.nhatrotot.rest.api.FirstRequestApi;

@RestController
@RequestMapping("/api")
public class FirstRequestController implements FirstRequestApi {

    @Override
    public ResponseEntity<String> helloWorld() {
        return new ResponseEntity<>("Hello World", HttpStatus.OK);
    }
}
