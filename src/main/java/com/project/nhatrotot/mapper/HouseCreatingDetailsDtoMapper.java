package com.project.nhatrotot.mapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.project.nhatrotot.model.House;
import com.project.nhatrotot.rest.dto.HouseCreatingDetailsDto;

@Mapper
public interface HouseCreatingDetailsDtoMapper {
    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "owner", expression = "java(null)")
    @Mapping(target = "houseImages", expression = "java(null)")
    @Mapping(target = "houseCategory", expression = "java(null)")
    @Mapping(target = "houseType", expression = "java(null)")
    @Mapping(target = "province", expression = "java(null)")
    @Mapping(target = "district", expression = "java(null)")
    @Mapping(target = "ward", expression = "java(null)")
    public House convertFromHouseCreatingDetailDto(HouseCreatingDetailsDto hDto);

    default LocalDateTime mapStartTime(OffsetDateTime offsetDate) {

        return offsetDate
                .atZoneSameInstant(ZoneId.systemDefault())
                .toLocalDateTime();
    }

}
