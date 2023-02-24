package com.project.nhatrotot.rest.controllers;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.nhatrotot.rest.api.AuthApi;
import com.project.nhatrotot.rest.dto.ChangePasswordHandleRequestDto;
import com.project.nhatrotot.rest.dto.ResetPasswordHandleRequestDto;
import com.project.nhatrotot.rest.dto.ResetSAPasswordHandleRequestDto;
import com.project.nhatrotot.rest.dto.UserCreationFieldsDto;
import com.project.nhatrotot.service.UserService;

@RestController
@RequestMapping("/api")
public class AuthController implements AuthApi {
    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<Void> resetPasswordHandle(
            @Valid ResetPasswordHandleRequestDto resetPasswordHandleRequestDto) {
        userService.resetUserPassword(resetPasswordHandleRequestDto.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> signUpHandle(@Valid UserCreationFieldsDto userCreationFieldsDto) {
        userService.registerClient(userCreationFieldsDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @PreAuthorize("hasAnyRole('sub_admin')")
    public ResponseEntity<Void> createSubAdminHandle(@Valid UserCreationFieldsDto userCreationFieldsDto) {
        userService.registerSubAdmin(userCreationFieldsDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @PreAuthorize("hasAnyRole('admin','sub_admin')")
    public ResponseEntity<Void> banUserHandle(UUID userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_admin"));
        userService.banUser(userId, isAdmin);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @PreAuthorize("hasAnyRole('admin')")
    public ResponseEntity<Void> resetSAPasswordHandle(UUID userId,
            @Valid ResetSAPasswordHandleRequestDto resetSAPasswordHandleRequestDto) {
                JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication();
        boolean isSubAdmin = authenticationToken.getAuthorities().stream().anyMatch(a -> a.toString().equals("ROLE_sub_admin"));
        userService.resetSAPassword(userId, resetSAPasswordHandleRequestDto.getPassword(), isSubAdmin);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> changePasswordHandle(
            @Valid ChangePasswordHandleRequestDto changePasswordHandleRequestDto) {
        // TODO Auto-generated method stub
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String userId = (String) jwt.getClaims().get("sub");
        String email = (String) jwt.getClaims().get("email");
        userService.changePassword(email, userId, changePasswordHandleRequestDto.getNewPassword(),
                changePasswordHandleRequestDto.getOldPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
