package com.project.nhatrotot.rest.controllers;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.nhatrotot.rest.api.StatisticsApi;
import com.project.nhatrotot.rest.dto.GetHouseStatistics200ResponseInnerDto;
import com.project.nhatrotot.rest.dto.GetPurchaseStatistics200ResponseInnerDto;
import com.project.nhatrotot.rest.dto.Top10MostPurchasedDto;
import com.project.nhatrotot.service.StatisticsService;

@RestController
@RequestMapping("/api")
public class StatisticsController implements StatisticsApi {

    @Autowired
    private StatisticsService statisticsService;

    @Override
    @PreAuthorize("hasAnyRole('admin','sub_admin')")
    public ResponseEntity<List<GetHouseStatistics200ResponseInnerDto>> getHouseStatistics(
            @NotNull @Valid String separateBy, @NotNull @Valid LocalDate dateFrom, @NotNull @Valid LocalDate dateTo,
            @Min(1) @Max(5) @Valid Integer type, @Min(1) @Max(2) @Valid Integer category) {
        var result = statisticsService.getHouseStatistics(separateBy, dateFrom, dateTo, type, category);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    @PreAuthorize("hasAnyRole('admin','sub_admin')")
    public ResponseEntity<List<GetPurchaseStatistics200ResponseInnerDto>> getPurchaseStatistics(
            @NotNull @Valid String separateBy, @NotNull @Valid LocalDate dateFrom, @NotNull @Valid LocalDate dateTo,
            @Min(1) @Max(2) @Valid Integer type) {
        // TODO Auto-generated method stub
        var result = statisticsService.getPurchaseStatistics(separateBy,
                dateFrom, dateTo, type);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    @PreAuthorize("hasAnyRole('admin','sub_admin')")
    public ResponseEntity<List<Top10MostPurchasedDto>> getTopPurchase(@NotNull @Valid LocalDate dateFrom,
            @NotNull @Valid LocalDate dateTo) {
        var result = statisticsService.getListTopPurchase(
                dateFrom, dateTo);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
