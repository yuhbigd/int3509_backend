package com.project.nhatrotot.mapper;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.project.nhatrotot.model.HouseES;
import com.project.nhatrotot.rest.dto.HouseSearchDetailsDto;

@Mapper
public interface HouseSearchDetailDtoMapper {
    @Mapping(source = "visible", target = "isVisible", qualifiedByName = "IntegerToBoolean")
    public HouseSearchDetailsDto convertFromHouseES(HouseES houseES);

    public List<HouseSearchDetailsDto> convertFromListHouseES(List<HouseES> houseES);

    @Named("IntegerToBoolean")
    public static Boolean IntegerToBoolean(Integer value) {
        if (value.intValue() > 0) {
            return true;
        }
        return false;
    }

    default OffsetDateTime localDateToOffset(Date date) {
        ZoneOffset zoneOffset = ZoneOffset.of("+07:00");
        return date.toInstant().atOffset(zoneOffset);
    }
}
