package com.project.nhatrotot.mapper;

import org.mapstruct.Mapper;

import com.project.nhatrotot.model.statistics.TotalHousesCreatedStatisticsByMonth;
import com.project.nhatrotot.rest.dto.TotalHouseCreatedStatisticsByMonthDto;

import java.util.List;

@Mapper
public interface TotalHouseCreatedStatisticsByMonthMapper {
    TotalHouseCreatedStatisticsByMonthDto convertFromTotalHouseCreatedStatisticsByMonth(
            TotalHousesCreatedStatisticsByMonth house);

    List<TotalHouseCreatedStatisticsByMonthDto> convertListFromTotalHouseCreatedStatisticsByMonth(
            List<TotalHousesCreatedStatisticsByMonth> houses);

}
