package com.project.nhatrotot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import com.project.nhatrotot.model.House;
import com.project.nhatrotot.model.HouseImages;
import com.project.nhatrotot.rest.dto.HouseDto;

@Mapper
public interface HouseDtoMapper {
    @Mapping(source = "houseCategory.id", target = "houseCategory")
    @Mapping(source = "houseType.id", target = "houseType")
    @Mapping(source = "province.name", target = "province")
    @Mapping(source = "ward.name", target = "ward")
    @Mapping(source = "district.name", target = "district")
    @Mapping(target = "images", source = "houseImages")
    @Mapping(target = "owner.email", source = "owner.email")
    @Mapping(target = "owner.userImage", source = "owner.image")
    @Mapping(target = "owner.userRating", source = "owner.avgRating")
    @Mapping(target = "owner.lastName", source = "owner.lastName")
    @Mapping(target = "owner.firstName", source = "owner.firstName")
    @Mapping(target = "owner.phoneNumber", source = "owner.phoneNumber")
    HouseDto convertFromHouse(House house);

    List<HouseDto> convertFromListHouses(List<House> houses);

    default OffsetDateTime localDateToOffset(LocalDateTime date) {
        ZoneId zoneId = ZoneId.systemDefault();
        return ZonedDateTime.of(date, zoneId).toOffsetDateTime();
    }

    default String convertFromHouseImages(HouseImages houseImages) {
        return houseImages.getUrl();
    }
}
