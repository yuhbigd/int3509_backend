package com.project.nhatrotot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.project.nhatrotot.model.Districts;
import com.project.nhatrotot.rest.dto.DistrictDto;
import java.util.List;

@Mapper
public interface DistrictDtoMapper {
    @Mapping(target = "provinceCode", source = "province.code")
    @Mapping(target = "province", source = "province.fullName")
    @Mapping(source = "administrativeUnits.fullName", target = "unitTitle")
    DistrictDto convertFromDistrict(Districts district);

    List<DistrictDto> convertFromListDistrict(List<Districts> district);
}
