package com.project.nhatrotot.rest.controllers;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import com.project.nhatrotot.model.Gender;
import com.project.nhatrotot.rest.api.UsersApi;
import com.project.nhatrotot.rest.dto.AddRatingHandleRequestDto;
import com.project.nhatrotot.rest.dto.ChangeUserTitleHandleRequestDto;
import com.project.nhatrotot.rest.dto.UserFieldsDto;
import com.project.nhatrotot.rest.dto.UserInformationDto;
import com.project.nhatrotot.rest.dto.UserInformationPageDto;
import com.project.nhatrotot.rest.dto.UserPublicInformationDto;
import com.project.nhatrotot.rest.dto.UserPublicInformationPageDto;
import com.project.nhatrotot.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController implements UsersApi {
    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<UserPublicInformationDto> getUserInformation(UUID userId) {
        UserPublicInformationDto userDto = userService.getUserInformationFromId(userId.toString());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserInformationDto> getMyInformation() {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String userId = (String) jwt.getClaims().get("sub");
        UserInformationDto uDto = userService.getDetailsUserInfor(userId);
        return new ResponseEntity<>(uDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> changeUserInformationHandler(@Valid UserFieldsDto userFieldsDto) {
        // TODO Auto-generated method stub
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String userId = (String) jwt.getClaims().get("sub");
        Gender gender = Gender.valueOf(userFieldsDto.getGender().toString());
        String phoneNumber = userFieldsDto.getPhoneNumber();
        String image = userFieldsDto.getImage();
        String intro = userFieldsDto.getIntro();
        LocalDateTime birthDate = userFieldsDto.getBirthDate().atStartOfDay();
        userService.changeInformation(userId, gender, phoneNumber, intro, image, birthDate);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('admin','sub_admin')")
    @Override
    public ResponseEntity<UserInformationDto> adminGetUserInformation(UUID userId) {
        UserInformationDto uDto = userService.getDetailsUserInfor(userId.toString());
        return new ResponseEntity<>(uDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('admin','sub_admin')")
    @Override
    public ResponseEntity<UserInformationPageDto> adminGetListUserInformation(@Min(0) @Valid Integer page,
            @Min(1) @Valid Integer size, @Valid String email, @Min(1) @Max(3) @Valid Long role) {

        UserInformationPageDto uPageDto = userService.getPrivateUserInfoPage(page,
                size, email, role);
        return new ResponseEntity<>(uPageDto, HttpStatus.OK);
    }

    @Override
    @PreAuthorize("hasAnyRole('admin','sub_admin')")
    public ResponseEntity<Void> changeUserTitleHandle(UUID userId,
            @Valid ChangeUserTitleHandleRequestDto changeUserTitleHandleRequestDto) {
        // TODO Auto-generated method stub
        userService.changeUserTitle(changeUserTitleHandleRequestDto.getTitle(), userId.toString());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> addRatingHandle(@Valid AddRatingHandleRequestDto addRatingHandleRequestDto) {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String fromId = (String) jwt.getClaims().get("sub");
        userService.addUserRating(fromId, addRatingHandleRequestDto.getUserId().toString(),
                addRatingHandleRequestDto.getRating());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserPublicInformationPageDto> getListUserInformation(@Min(0) @Valid Integer page,
            @Min(1) @Valid Integer size, @Valid String searchPhrase) {
        UserPublicInformationPageDto uPageDto = userService.getUsersInfoPage(page, size, searchPhrase);
        return new ResponseEntity<>(uPageDto, HttpStatus.OK);
    }

}
