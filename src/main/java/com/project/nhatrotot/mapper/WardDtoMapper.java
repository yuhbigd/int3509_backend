package com.project.nhatrotot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.project.nhatrotot.model.Wards;
import com.project.nhatrotot.rest.dto.WardDto;
import java.util.List;

@Mapper
public interface WardDtoMapper {
    @Mapping(target = "districtCode", source = "district.code")
    @Mapping(target = "district", source = "district.fullName")
    @Mapping(source = "administrativeUnits.fullName", target = "unitTitle")
    WardDto convertFromWard(Wards district);

    List<WardDto> convertFromListWard(List<Wards> district);
}
