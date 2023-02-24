package com.project.nhatrotot.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ValueMapping;
import org.mapstruct.ValueMappings;

import com.project.nhatrotot.model.UserEntity;
import com.project.nhatrotot.rest.dto.UserInformationDto;

@Mapper
public interface UserInformationMapper {
    @ValueMappings({
            @ValueMapping(source = "male", target = "MALE"),
            @ValueMapping(source = "female", target = "FEMALE"),
            @ValueMapping(source = "other", target = "OTHER"),
    })
    @Mapping(target = "title", source = "title.title")
    @Mapping(target = "role", source = "role.role")
    UserInformationDto convertFromUserEntity(UserEntity userEntity);

    List<UserInformationDto> convertFromUserEntityList(List<UserEntity> userList);

    default OffsetDateTime localDateToOffset(LocalDateTime date) {
        ZoneId zoneId = ZoneId.systemDefault();
        return ZonedDateTime.of(date, zoneId).toOffsetDateTime();
    }

    default LocalDate localDateTimeToLocalDate(LocalDateTime date) {
        return date.toLocalDate();
    }
}
