package com.project.nhatrotot.mapper;

import org.mapstruct.Mapper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import com.project.nhatrotot.model.statistics.PurchaseStatisticsByDate;
import com.project.nhatrotot.rest.dto.PurchaseStatisticsByDateDto;

@Mapper
public interface PurchaseStatisticsByDateMapper {
    PurchaseStatisticsByDateDto convertFromPurchaseStatisticsByDate(PurchaseStatisticsByDate stats);

    List<PurchaseStatisticsByDateDto> convertToListFromPurchaseStatisticsByDate(List<PurchaseStatisticsByDate> stats);

    default LocalDate convert(Date date) {
        Date safeDate = new Date(date.getTime());

        return safeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
