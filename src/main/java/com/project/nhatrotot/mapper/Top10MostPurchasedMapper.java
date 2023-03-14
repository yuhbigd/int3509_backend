package com.project.nhatrotot.mapper;

import org.mapstruct.Mapper;

import com.project.nhatrotot.model.statistics.Top10MostPurchased;
import com.project.nhatrotot.rest.dto.Top10MostPurchasedDto;

import java.util.List;

@Mapper
public interface Top10MostPurchasedMapper {
    Top10MostPurchasedDto convertFromEntity(Top10MostPurchased user);

    List<Top10MostPurchasedDto> convertFromListEntity(List<Top10MostPurchased> users);
}
