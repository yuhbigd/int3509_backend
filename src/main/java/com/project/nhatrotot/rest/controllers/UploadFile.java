package com.project.nhatrotot.rest.controllers;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.project.nhatrotot.rest.api.FileApi;
import com.project.nhatrotot.service.file_service.FileUploadService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class UploadFile implements FileApi {
    private Map<String, SseEmitter> sseMap = new ConcurrentHashMap<>();
    @Autowired
    private FileUploadService fileUploadService;

    @GetMapping(value = "/progress")
    public SseEmitter getMethodName() throws IOException {
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        UUID guid = UUID.randomUUID();
        sseEmitter.send(SseEmitter.event().name("GUID").data(guid.toString()));
        sseEmitter.onCompletion(() -> sseMap.remove(guid.toString()));
        sseEmitter.onTimeout(() -> sseMap.remove(guid.toString()));
        sseMap.put(guid.toString(), sseEmitter);
        return sseEmitter;
    }

    @GetMapping(value = "/auth/login")
    String test() {
        return "test login auth";
    }

    @Override
    public ResponseEntity<String> postFileHandle(MultipartFile file, @Valid String fileType, @Valid UUID GUID) {
        // TODO Auto-generated method stub
        SseEmitter sseEmitter = sseMap.get(GUID.toString());
        if (sseEmitter == null) {
            return new ResponseEntity<>("GUID invalid", HttpStatus.NOT_FOUND);
        }
        String result = "ERROR";
        try {
            result = fileUploadService.upload(file, fileType, sseEmitter);
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
