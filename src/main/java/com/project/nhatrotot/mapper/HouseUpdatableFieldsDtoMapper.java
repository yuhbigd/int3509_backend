package com.project.nhatrotot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.project.nhatrotot.model.House;
import com.project.nhatrotot.model.HouseImages;
import com.project.nhatrotot.rest.dto.HouseUpdatableFieldsDto;

@Mapper
public interface HouseUpdatableFieldsDtoMapper {
    @Mapping(target = "houseImages", source = "images")
    House convertFromHouseUpdatableFields(HouseUpdatableFieldsDto houseDto);

    default HouseImages convertToHouseImages(String image) {
        var hImage = new HouseImages();
        hImage.setUrl(image);
        return hImage;
    }
}
