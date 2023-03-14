package com.project.nhatrotot.mapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ValueMapping;
import org.mapstruct.ValueMappings;

import com.project.nhatrotot.model.UserEntity;
import com.project.nhatrotot.rest.dto.UserCreationFieldsDto;
import java.time.LocalDate;

@Mapper
public interface UserCreationMapper {
    @ValueMappings({
            @ValueMapping(source = "MALE", target = "male"),
            @ValueMapping(source = "FEMALE", target = "female"),
            @ValueMapping(source = "OTHER", target = "other"),
    })
    @Mapping(target = "userId", expression = "java(null)")
    @Mapping(target = "role", expression = "java(null)")
    @Mapping(target = "title", expression = "java(null)")
    @Mapping(target = "balance", constant = "0")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "banned", constant = "false")
    @Mapping(target = "registerAt", expression = "java(null)")
    UserEntity convertToUserModelEntity(UserCreationFieldsDto userDto);

    default LocalDateTime mapStartTime(OffsetDateTime offsetDate) {

        return offsetDate
                .atZoneSameInstant(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    default LocalDateTime mapLocalDateTime(LocalDate date) {
        return date.atStartOfDay();

    }
}
