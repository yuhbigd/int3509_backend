package com.project.nhatrotot.mapper;

import org.mapstruct.Mapper;

import com.project.nhatrotot.model.UserRole;
import com.project.nhatrotot.rest.dto.UserRoleDto;

// @Mapper
public interface UserRoleMapper {
    UserRole toUserRole(UserRoleDto dto);
    UserRoleDto toUserRoleDto(UserRole userRole);
}
