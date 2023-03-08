package com.project.nhatrotot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.project.nhatrotot.model.Provinces;
import com.project.nhatrotot.rest.dto.ProvinceDto;
import java.util.List;

@Mapper
public interface ProvinceDtoMapper {
    @Mapping(source = "administrativeUnits.fullName", target = "unitTitle")
    @Mapping(source = "administrativeRegion.name", target = "region")
    ProvinceDto convertFromProvinces(Provinces province);

    List<ProvinceDto> convertFromListProvinces(List<Provinces> province);
}
