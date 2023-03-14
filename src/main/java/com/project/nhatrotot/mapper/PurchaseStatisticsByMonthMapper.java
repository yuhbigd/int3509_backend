package com.project.nhatrotot.mapper;

import org.mapstruct.Mapper;
import java.util.List;

import com.project.nhatrotot.model.statistics.PurchaseStatisticsByMonth;
import com.project.nhatrotot.rest.dto.PurchaseStatisticsByMonthDto;

@Mapper
public interface PurchaseStatisticsByMonthMapper {
    PurchaseStatisticsByMonthDto convertFromPurchaseStatisticsByMonth(PurchaseStatisticsByMonth stats);

    List<PurchaseStatisticsByMonthDto> convertToListFromPurchaseStatisticsByMonth(
            List<PurchaseStatisticsByMonth> stats);
}
