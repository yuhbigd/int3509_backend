package com.project.nhatrotot.mapper;

import org.mapstruct.Mapper;

import com.project.nhatrotot.model.HouseES;
import com.project.nhatrotot.rest.dto.HouseSearchDetailsDto;
import java.util.List;

@Mapper
public interface HouseSearchDetailDtoMapper {
    public HouseSearchDetailsDto convertFromHouseES(HouseES houseES);

    public List<HouseSearchDetailsDto> convertFromListHouseES(List<HouseES> houseES);
}
