package com.project.nhatrotot.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.nhatrotot.mapper.PurchaseStatisticsByDateMapper;
import com.project.nhatrotot.mapper.PurchaseStatisticsByMonthMapper;
import com.project.nhatrotot.mapper.Top10MostPurchasedMapper;
import com.project.nhatrotot.mapper.TotalHouseCreatedStatisticsByDateMapper;
import com.project.nhatrotot.mapper.TotalHouseCreatedStatisticsByMonthMapper;
import com.project.nhatrotot.model.HouseType;
import com.project.nhatrotot.model.HouseCategory;
import com.project.nhatrotot.model.PaymentState;
import com.project.nhatrotot.model.PaymentType;
import com.project.nhatrotot.repository.jpa.HouseRepository;
import com.project.nhatrotot.repository.jpa.PaymentRepository;
import com.project.nhatrotot.rest.dto.GetHouseStatistics200ResponseInnerDto;
import com.project.nhatrotot.rest.dto.GetPurchaseStatistics200ResponseInnerDto;
import com.project.nhatrotot.rest.dto.Top10MostPurchasedDto;

@Service
public class StatisticsService {
        @Autowired
        private PurchaseStatisticsByDateMapper purchaseStatisticsByDateMapper;
        @Autowired
        private PurchaseStatisticsByMonthMapper purchaseStatisticsByMonthMapper;
        @Autowired
        private TotalHouseCreatedStatisticsByDateMapper totalHouseCreatedStatisticsByDateMapper;
        @Autowired
        private TotalHouseCreatedStatisticsByMonthMapper totalHouseCreatedStatisticsByMonthMapper;
        @Autowired
        private Top10MostPurchasedMapper top10MostPurchasedMapper;
        @Autowired
        private HouseRepository houseRepository;
        @Autowired
        private PaymentRepository paymentRepository;

        public List<GetPurchaseStatistics200ResponseInnerDto> getPurchaseStatistics(String separateBy,
                        LocalDate dateFrom,
                        LocalDate dateTo, Integer type) {

                if (separateBy.equals("date")) {
                        if (type != null) {
                                var queryResult = paymentRepository.findPurchaseStatisticsByDateWithType(
                                                PaymentState.done,
                                                dateFrom.toString(), dateTo.toString(), new PaymentType(type, null));
                                List<GetPurchaseStatistics200ResponseInnerDto> result = List
                                                .copyOf(purchaseStatisticsByDateMapper
                                                                .convertToListFromPurchaseStatisticsByDate(
                                                                                queryResult));
                                return result;
                        } else {
                                var queryResult = paymentRepository.findPurchaseStatisticsByDate(PaymentState.done,
                                                dateFrom.toString(), dateTo.toString());

                                List<GetPurchaseStatistics200ResponseInnerDto> result = List
                                                .copyOf(purchaseStatisticsByDateMapper
                                                                .convertToListFromPurchaseStatisticsByDate(
                                                                                queryResult));
                                return result;
                        }
                } else if (separateBy.equals("month")) {
                        if (type != null) {
                                var queryResult = paymentRepository.findPurchaseStatisticsByMonthWithType(
                                                PaymentState.done,
                                                dateFrom.toString(), dateTo.toString(), new PaymentType(type, null));
                                List<GetPurchaseStatistics200ResponseInnerDto> result = List
                                                .copyOf(purchaseStatisticsByMonthMapper
                                                                .convertToListFromPurchaseStatisticsByMonth(
                                                                                queryResult));
                                return result;
                        } else {
                                var queryResult = paymentRepository.findPurchaseStatisticsByMonth(PaymentState.done,
                                                dateFrom.toString(), dateTo.toString());
                                List<GetPurchaseStatistics200ResponseInnerDto> result = List
                                                .copyOf(purchaseStatisticsByMonthMapper
                                                                .convertToListFromPurchaseStatisticsByMonth(
                                                                                queryResult));
                                return result;
                        }
                }
                return new ArrayList<>();
        }

        public List<GetHouseStatistics200ResponseInnerDto> getHouseStatistics(String separateBy, LocalDate dateFrom,
                        LocalDate dateTo,
                        Integer type, Integer category) {
                if (separateBy.equals("date")) {
                        if (type != null && category != null) {
                                var queryResult = houseRepository
                                                .findTotalHousesCreatedStatisticsByDateWithTypeAndCategory(
                                                                dateFrom.toString(), dateTo.toString(),
                                                                new HouseType(type, null),
                                                                new HouseCategory(category, null));
                                List<GetHouseStatistics200ResponseInnerDto> result = List
                                                .copyOf(totalHouseCreatedStatisticsByDateMapper
                                                                .convertListFromTotalHouseCreatedStatisticsByDate(
                                                                                queryResult));
                                return result;
                        } else {
                                var queryResult = houseRepository.findTotalHousesCreatedStatisticsByDate(
                                                dateFrom.toString(), dateTo.toString());
                                List<GetHouseStatistics200ResponseInnerDto> result = List
                                                .copyOf(totalHouseCreatedStatisticsByDateMapper
                                                                .convertListFromTotalHouseCreatedStatisticsByDate(
                                                                                queryResult));
                                return result;
                        }
                } else if (separateBy.equals("month")) {
                        if (type != null && category != null) {
                                var queryResult = houseRepository
                                                .findTotalHousesCreatedStatisticsByMonthWithTypeAndCategory(
                                                                dateFrom.toString(), dateTo.toString(),
                                                                new HouseType(type, null),
                                                                new HouseCategory(category, null));
                                List<GetHouseStatistics200ResponseInnerDto> result = List
                                                .copyOf(totalHouseCreatedStatisticsByMonthMapper
                                                                .convertListFromTotalHouseCreatedStatisticsByMonth(
                                                                                queryResult));
                                return result;
                        } else {
                                var queryResult = houseRepository.findTotalHousesCreatedStatisticsByMonth(
                                                dateFrom.toString(), dateTo.toString());
                                List<GetHouseStatistics200ResponseInnerDto> result = List
                                                .copyOf(totalHouseCreatedStatisticsByMonthMapper
                                                                .convertListFromTotalHouseCreatedStatisticsByMonth(
                                                                                queryResult));
                                return result;
                        }
                }
                return new ArrayList<>();
        }

        public List<Top10MostPurchasedDto> getListTopPurchase(LocalDate fromDate, LocalDate toDate) {
                var queryResult = paymentRepository.findTop10MostPurchased(PaymentState.done, fromDate.toString(),
                                toDate.toString());
                List<Top10MostPurchasedDto> result = top10MostPurchasedMapper.convertFromListEntity(queryResult);
                return result;
        }
}
