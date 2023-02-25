package com.project.nhatrotot.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.ws.rs.core.Response;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.nhatrotot.mapper.UserCreationMapper;
import com.project.nhatrotot.mapper.UserInformationMapper;
import com.project.nhatrotot.mapper.UserPublicInformationMapper;
import com.project.nhatrotot.model.Gender;
import com.project.nhatrotot.model.UserEntity;
import com.project.nhatrotot.model.UserRating;
import com.project.nhatrotot.model.UserTitle;
import com.project.nhatrotot.repository.jpa.UserEntityRepository;
import com.project.nhatrotot.repository.jpa.UserRatingRepository;
import com.project.nhatrotot.repository.jpa.UserRoleRepository;
import com.project.nhatrotot.repository.jpa.UserTitleRepository;
import com.project.nhatrotot.rest.advice.CustomException.CreateUserException;
import com.project.nhatrotot.rest.advice.CustomException.GeneralException;
import com.project.nhatrotot.rest.advice.CustomException.UserNotAllowedException;
import com.project.nhatrotot.rest.advice.CustomException.UserNotFoundException;
import com.project.nhatrotot.rest.dto.UserCreationFieldsDto;
import com.project.nhatrotot.rest.dto.UserInformationDto;
import com.project.nhatrotot.rest.dto.UserInformationPageDto;
import com.project.nhatrotot.rest.dto.UserPublicInformationDto;
import com.project.nhatrotot.rest.dto.UserPublicInformationPageDto;
import com.project.nhatrotot.util.constant.UserConstant;

@Service
public class UserService {
    @Value("${app.properties.keycloak.realm}")
    private String realm;

    Keycloak keycloak;

    private UserEntityRepository userRepository;
    private UserCreationMapper userCreationMapper;
    private UserTitleRepository userTitleRepository;
    private UserRatingRepository userRatingRepository;

    private UserRoleRepository userRoleRepository;
    private UserPublicInformationMapper userPublicInformationMapper;
    private UserInformationMapper userInformationMapper;

    private RequestUtilService requestUtil;

    public UserService(Keycloak keycloak, UserEntityRepository userRepository, UserCreationMapper userCreationMapper,
            UserTitleRepository userTitleRepository, UserRoleRepository userRoleRepository,
            UserPublicInformationMapper userPublicInformationMapper, UserInformationMapper userInformationMapper,
            RequestUtilService requestUtil, UserRatingRepository userRatingRepository) {
        this.keycloak = keycloak;
        this.userRepository = userRepository;
        this.userCreationMapper = userCreationMapper;
        this.userTitleRepository = userTitleRepository;
        this.userRoleRepository = userRoleRepository;
        this.userPublicInformationMapper = userPublicInformationMapper;
        this.userInformationMapper = userInformationMapper;
        this.requestUtil = requestUtil;
        this.userRatingRepository = userRatingRepository;
    }

    public void addUserRating(String fromId, String toId, int rating) {
        Optional<UserEntity> toUser = userRepository.findById(toId);
        if (!toUser.isPresent()) {
            throw new UserNotFoundException("User not found", HttpStatus.NOT_FOUND);
        }
        Optional<UserRating> existedRating = userRatingRepository.findByUser_userIdAndFromId(toId, fromId);
        if (existedRating.isPresent()) {
            throw new GeneralException("User has rated that user before", HttpStatus.BAD_REQUEST);
        }
        UserRating savedRating = new UserRating();
        savedRating.setFromId(fromId);
        savedRating.setUser(toUser.get());
        savedRating.setRating(rating);
        userRatingRepository.save(savedRating);
    }

    @Transactional
    public void changeInformation(String userId, Gender gender, String phoneNumber, String intro, String image,
            LocalDateTime birthDate) {
        userRepository.updateInformation(userId, gender, phoneNumber, intro, image, birthDate);
    }

    @Transactional
    public void changeUserTitle(Long titleId, String userId) {
        Optional<UserTitle> titleOptional = userTitleRepository.findById(titleId);
        var title = titleOptional.get();
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("user not found", HttpStatus.NOT_FOUND);
        }
        var user = userOptional.get();
        if (user.getRole().getRole() == UserConstant.ROLE_ADMIN) {
            throw new GeneralException("this is admin user bro", HttpStatus.FORBIDDEN);
        }
        userRepository.updateTitle(userId, title);
    }

    public void changePassword(String email, String userId, String newPass, String oldPass) {
        HttpStatus checkedStatus = requestUtil.requestCheckOldPassword(email, oldPass);
        if (!checkedStatus.is2xxSuccessful()) {
            throw new GeneralException("current password is incorrect", checkedStatus);
        }
        changePassword(userId, newPass);
    }

    public UserInformationDto getDetailsUserInfor(String userId) {
        var userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            return userInformationMapper.convertFromUserEntity(userOpt.get());
        }
        throw new UserNotFoundException("User not found", HttpStatus.NOT_FOUND);
    }

    public UserPublicInformationPageDto getUsersInfoPage(int page, int size, String searchPhrase) {
        Pageable pageable = PageRequest.of(page, size);
        var usersPage = userRepository
                .findByEmailContainingOrFirstNameContainingOrLastNameContainingOrPhoneNumberContainingOrEmailContainingAllIgnoreCase(
                        searchPhrase, searchPhrase, searchPhrase, searchPhrase, searchPhrase, pageable);
        var totalPage = usersPage.getTotalPages();
        var users = userPublicInformationMapper.convertFromUserEntityList(usersPage.getContent());
        var result = new UserPublicInformationPageDto();
        result.setCurrentPage(page);
        result.setTotalPage(totalPage);
        result.setUsers(users);
        return result;
    }

    public UserInformationPageDto getPrivateUserInfoPage(int page, int size, String searchPhrase, Long roleId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserEntity> usersPage;
        if (roleId != null) {
            usersPage = userRepository.findByEmailContainingIgnoreCaseAndRole_id(searchPhrase, roleId, pageable);
        } else {
            usersPage = userRepository.findByEmailContainingIgnoreCase(searchPhrase, pageable);
        }
        var totalPage = usersPage.getTotalPages();
        var users = userInformationMapper.convertFromUserEntityList(usersPage.getContent());
        var result = new UserInformationPageDto();
        result.setCurrentPage(page);
        result.setTotalPage(totalPage);
        result.setUsers(users);
        return result;
    }

    public UserPublicInformationDto getUserInformationFromId(String userId) {
        var userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            return userPublicInformationMapper.convertFromUserEntity(userOpt.get());
        }
        throw new UserNotFoundException("User not found", HttpStatus.NOT_FOUND);
    }

    public void registerClient(UserCreationFieldsDto uDto) {
        registerUser(uDto, UserConstant.TITLE_CLIENT, UserConstant.ROLE_CLIENT);

    }

    public void registerSubAdmin(UserCreationFieldsDto uDto) {
        registerUser(uDto, UserConstant.TITLE_ADMIN, UserConstant.ROLE_SUB_ADMIN);
    }

    public void resetSAPassword(UUID userId, String password, boolean isSubAdmin) {
        if (!isSubAdmin) {
            throw new UserNotFoundException("this user isn't a sub admin");
        }
        changePassword(userId.toString(), password);
    }

    @Transactional
    public void banUser(UUID userId, boolean isAdmin) {
        var uEntity = userRepository.findById(userId.toString());
        if (!uEntity.isPresent()) {
            throw new UserNotFoundException("user not found");
        }
        UserEntity user = uEntity.get();
        if (user.getRole().getRole().equals(UserConstant.ROLE_ADMIN)
                || (user.getRole().getRole().equals(UserConstant.ROLE_SUB_ADMIN) && !isAdmin)) {
            throw new UserNotAllowedException("Forbidden", HttpStatus.FORBIDDEN);
        }
        boolean isBanned = user.getBanned();
        var usersResource = keycloak.realm(realm).users().get(user.getUserId());
        UserRepresentation userKc = usersResource.toRepresentation();
        userKc.setEnabled(isBanned);
        usersResource.update(userKc);
        userRepository.deactivatedUsers(user.getUserId(), !isBanned);
        usersResource.logout();
    }

    public void resetUserPassword(String userEmail) {
        UsersResource usersResource = keycloak.realm(realm).users();
        Optional<UserRepresentation> user = usersResource.search(userEmail, 0, 2).stream()
                .filter(u -> {
                    return u.getEmail().equals(userEmail);
                }).findFirst();
        if (user.isPresent()) {
            String uuid = user.get().getId();
            usersResource.get(uuid).executeActionsEmail(List.of("UPDATE_PASSWORD"));
        } else {
            throw new UserNotFoundException("Email not valid");
        }
    }

    private void registerUser(UserCreationFieldsDto uDto, String title, String role) {
        UsersResource usersResource = keycloak.realm(realm).users();
        Response userCreationResponse = createUserInKeyCloak(usersResource, uDto);
        if (userCreationResponse.getStatus() == 409) {
            throw new CreateUserException("client already exists");
        } else if (userCreationResponse.getStatus() != 201) {
            throw new CreateUserException("There is an error when create client");
        }
        String responseUserUrl = userCreationResponse.getHeaderString("Location");
        String userId = responseUserUrl.substring(responseUserUrl.length() - 36);
        setUserRole(role, userId, usersResource);
        UserEntity user = saveUser(uDto, title, role, userId);
        if (user == null) {
            usersResource.delete(userId);
        }
    }

    private Response createUserInKeyCloak(UsersResource usersResource, UserCreationFieldsDto uDto) {
        CredentialRepresentation cRepresentation = new CredentialRepresentation();
        cRepresentation.setTemporary(false);
        cRepresentation.setType(CredentialRepresentation.PASSWORD);
        cRepresentation.setValue(uDto.getPassword());
        UserRepresentation keycloakUser = new UserRepresentation();
        keycloakUser.setEmail(uDto.getEmail());
        keycloakUser.setEmailVerified(true);
        keycloakUser.setCredentials(Collections.singletonList(cRepresentation));
        keycloakUser.setFirstName(uDto.getFirstName());
        keycloakUser.setLastName(uDto.getLastName());
        keycloakUser.setEnabled(true);
        return usersResource.create(keycloakUser);
    }

    private void setUserRole(String role, String userId, UsersResource usersResource) {
        switch (role) {
            case UserConstant.ROLE_ADMIN:
            case UserConstant.ROLE_SUB_ADMIN:
            case UserConstant.ROLE_CLIENT:
                break;
            default:
                throw new CreateUserException("invalid role");
        }
        RoleRepresentation roleRepresentation = keycloak.realm(realm).roles().get(role).toRepresentation();
        usersResource.get(userId).roles().realmLevel().add(Arrays.asList(roleRepresentation));
    }

    private UserEntity saveUser(UserCreationFieldsDto uDto, String title, String role, String userId) {
        UserEntity userEntity = userCreationMapper.convertToUserModelEntity(uDto);
        userEntity.setUserId(userId);
        userEntity.setAvgRating(0);
        userEntity.setBalance(new BigDecimal(0));
        userEntity.setBanned(false);
        var userRole = userRoleRepository.findById(UserConstant.USER_ROLES_MAP.get(role));
        var userTitle = userTitleRepository.findById(UserConstant.USER_TITLE_MAP.get(title));
        if (userRole.isPresent() && userTitle.isPresent()) {
            var ro = userRole.get();
            var t = userTitle.get();
            userEntity.setRole(ro);
            userEntity.setTitle(t);
            return userRepository.save(userEntity);
        }
        return null;
    }

    private void changePassword(String userId, String newPass) {
        UsersResource usersResource = keycloak.realm(realm).users();
        var userKc = usersResource.get(userId);
        var userRepresentation = userKc.toRepresentation();
        CredentialRepresentation cRepresentation = new CredentialRepresentation();
        cRepresentation.setTemporary(false);
        cRepresentation.setType(CredentialRepresentation.PASSWORD);
        cRepresentation.setValue(newPass);
        userRepresentation.setCredentials(Collections.singletonList(cRepresentation));
        userKc.update(userRepresentation);
        userKc.logout();
    }
}
