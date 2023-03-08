package com.project.nhatrotot.rest.controllers;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.project.nhatrotot.rest.advice.CustomException.GUIDInvalidException;
import com.project.nhatrotot.rest.advice.CustomException.GeneralException;
import com.project.nhatrotot.rest.api.FileApi;
import com.project.nhatrotot.rest.dto.BulkDeleteFilesHandleRequestDto;
import com.project.nhatrotot.service.file_service.FileUploadService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api")
public class UploadFile implements FileApi {
    private Map<String, SseEmitter> sseMap = new ConcurrentHashMap<>();
    @Autowired
    private FileUploadService fileUploadService;

    @GetMapping(value = "/progress")
    public SseEmitter getMethodName() throws IOException {
        SseEmitter sseEmitter = new SseEmitter(900000L);
        UUID guid = UUID.randomUUID();
        sseEmitter.send(SseEmitter.event().name("GUID").data(guid.toString()));
        sseEmitter.onCompletion(() -> sseMap.remove(guid.toString()));
        sseEmitter.onTimeout(() -> sseMap.remove(guid.toString()));
        sseMap.put(guid.toString(), sseEmitter);
        return sseEmitter;
    }

    @Override
    public ResponseEntity<String> postFileHandle(MultipartFile file, @Valid String fileType, @Valid UUID GUID) {
        // TODO Auto-generated method stub
        SseEmitter sseEmitter = sseMap.get(GUID.toString());
        if (sseEmitter == null) {
            throw new GUIDInvalidException("GUID not found");
        }
        String result = "ERROR";
        try {
            JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                    .getAuthentication();
            Jwt jwt = (Jwt) authenticationToken.getCredentials();
            String userId = (String) jwt.getClaims().get("sub");
            result = fileUploadService.upload(file, fileType, sseEmitter, userId);
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> bulkDeleteFilesHandle(
            @Valid BulkDeleteFilesHandleRequestDto bulkDeleteFilesHandleRequestDto) {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String userId = (String) jwt.getClaims().get("sub");
        boolean isSubAdmin = authenticationToken.getAuthorities().stream()
                .anyMatch(a -> (a.toString().equals("ROLE_sub_admin") || a.toString().equals("ROLE_admin")));
        String[] imagesArray = bulkDeleteFilesHandleRequestDto.getImages().toArray(String[]::new);
        if (imagesArray.length > 0) {
            try {
                fileUploadService.deleteFiles(imagesArray, userId, isSubAdmin);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                throw new GeneralException(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
