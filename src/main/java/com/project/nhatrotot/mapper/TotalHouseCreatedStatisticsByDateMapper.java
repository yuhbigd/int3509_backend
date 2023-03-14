package com.project.nhatrotot.mapper;

import org.mapstruct.Mapper;

import com.project.nhatrotot.model.statistics.TotalHousesCreatedStatisticsByDate;
import com.project.nhatrotot.rest.dto.TotalHouseCreatedStatisticsByDateDto;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Mapper
public interface TotalHouseCreatedStatisticsByDateMapper {
    TotalHouseCreatedStatisticsByDateDto convertFromTotalHouseCreatedStatisticsByDate(
            TotalHousesCreatedStatisticsByDate house);

    List<TotalHouseCreatedStatisticsByDateDto> convertListFromTotalHouseCreatedStatisticsByDate(
            List<TotalHousesCreatedStatisticsByDate> houses);

    default LocalDate convert(Date date) {
        Date safeDate = new Date(date.getTime());

        return safeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
