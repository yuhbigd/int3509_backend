package com.project.nhatrotot.rest.controllers;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.nhatrotot.model.Gender;
import com.project.nhatrotot.rest.api.UsersApi;
import com.project.nhatrotot.rest.dto.AddRatingHandleRequestDto;
import com.project.nhatrotot.rest.dto.ChangeUserTitleHandleRequestDto;
import com.project.nhatrotot.rest.dto.CheckIsFollowing200ResponseDto;
import com.project.nhatrotot.rest.dto.FollowUserRequestDto;
import com.project.nhatrotot.rest.dto.GetMyDetailsPayments200ResponseDto;
import com.project.nhatrotot.rest.dto.MyPaymentsPageDto;
import com.project.nhatrotot.rest.dto.UserInformationDto;
import com.project.nhatrotot.rest.dto.UserInformationPageDto;
import com.project.nhatrotot.rest.dto.UserPublicInformationDto;
import com.project.nhatrotot.rest.dto.UserPublicInformationPageDto;
import com.project.nhatrotot.rest.dto.UserUpdatableFieldDto;
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
    public ResponseEntity<Void> changeUserInformationHandler(@Valid UserUpdatableFieldDto userUpdatableFieldDto) {
        // TODO Auto-generated method stub
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String token = jwt.getTokenValue();
        String userId = (String) jwt.getClaims().get("sub");
        Gender gender = Gender.valueOf(userUpdatableFieldDto.getGender().toString());
        String phoneNumber = userUpdatableFieldDto.getPhoneNumber();
        String image = userUpdatableFieldDto.getImage();
        String intro = userUpdatableFieldDto.getIntro();
        LocalDateTime birthDate = userUpdatableFieldDto.getBirthDate().atStartOfDay();
        String lastName = userUpdatableFieldDto.getLastName();
        String firstName = userUpdatableFieldDto.getFirstName();
        userService.changeInformation(userId, gender, phoneNumber, intro, image, birthDate, token, lastName, firstName);
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

    @Override
    public ResponseEntity<MyPaymentsPageDto> getMyPaymentsHandle(@Min(0) @Valid Integer page,
            @Min(1) @Valid Integer size, @Valid Integer type, @Valid String sortBy, @Valid String sortOrder) {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String userId = (String) jwt.getClaims().get("sub");
        var paymentsPage = userService.getPaymentsPageDto(page, size, type, userId, sortBy, sortOrder);
        return new ResponseEntity<>(paymentsPage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GetMyDetailsPayments200ResponseDto> getMyDetailsPayments(UUID paymentId) {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String userId = (String) jwt.getClaims().get("sub");
        GetMyDetailsPayments200ResponseDto response = userService
                .getMyDetailsPayments200ResponseDto(paymentId.toString(), userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> followUser(@Valid FollowUserRequestDto followUserRequestDto) {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String userID = (String) jwt.getClaims().get("sub");
        userService.followUser(userID, followUserRequestDto.getUserId().toString());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserPublicInformationPageDto> getAllFollowing(@Min(0) @Valid Integer page,
            @Min(1) @Valid Integer size) {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String userId = (String) jwt.getClaims().get("sub");
        var followingPage = userService.getFollowingUsers(userId, page, size);
        return new ResponseEntity<>(followingPage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> unfollowUser(@Valid FollowUserRequestDto followUserRequestDto) {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String userId = (String) jwt.getClaims().get("sub");
        userService.unfollowUser(userId, followUserRequestDto.getUserId().toString());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CheckIsFollowing200ResponseDto> checkIsFollowing(UUID userId) {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String thisUserId = (String) jwt.getClaims().get("sub");
        boolean isFollowing = userService.isFollowing(thisUserId, userId.toString());
        var res = new CheckIsFollowing200ResponseDto();
        res.setIsFollowing(isFollowing);
        return new ResponseEntity<>(res, HttpStatus.OK);

    }

}
