package com.project.nhatrotot.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.nhatrotot.model.House;
import com.project.nhatrotot.model.HouseCategory;
import com.project.nhatrotot.model.HouseType;

import org.springframework.data.domain.Pageable;
import com.project.nhatrotot.model.statistics.TotalHousesCreatedStatisticsByMonth;
import com.project.nhatrotot.model.statistics.TotalHousesCreatedStatisticsByDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;

public interface HouseRepository extends JpaRepository<House, Integer> {
        Page<House> findByOwner_UserIdEquals(String userId, Pageable pageable);

        @Query("SELECT" +
                        " new com.project.nhatrotot.model.statistics.TotalHousesCreatedStatisticsByDate(DATE(h.createdDate), COUNT(h.id)) "
                        +
                        "FROM House h " +
                        "WHERE DATE(h.createdDate) between DATE(?1) and DATE(?2) " +
                        "GROUP BY DATE(h.createdDate) ORDER BY DATE(h.createdDate) DESC")
        List<TotalHousesCreatedStatisticsByDate> findTotalHousesCreatedStatisticsByDate(String dateFrom, String dateTo);

        @Query("SELECT" +
                        " new com.project.nhatrotot.model.statistics.TotalHousesCreatedStatisticsByDate(DATE(h.createdDate), COUNT(h.id)) "
                        +
                        "FROM House h " +
                        "WHERE DATE(h.createdDate) between DATE(?1) and DATE(?2) AND h.houseType = ?3 AND h.houseCategory = ?4 "
                        +
                        "GROUP BY DATE(h.createdDate) ORDER BY DATE(h.createdDate) DESC")
        List<TotalHousesCreatedStatisticsByDate> findTotalHousesCreatedStatisticsByDateWithTypeAndCategory(
                        String dateFrom, String dateTo, HouseType type, HouseCategory category);

        @Query("SELECT" +
                        " new com.project.nhatrotot.model.statistics.TotalHousesCreatedStatisticsByMonth(YEAR(h.createdDate), MONTH(h.createdDate), COUNT(h.id)) "
                        +
                        "FROM House h " +
                        "WHERE DATE(h.createdDate) between DATE(?1) and DATE(?2) " +
                        "GROUP BY YEAR(h.createdDate), MONTH(h.createdDate) ORDER BY YEAR(h.createdDate) DESC, MONTH(h.createdDate) DESC")
        List<TotalHousesCreatedStatisticsByMonth> findTotalHousesCreatedStatisticsByMonth(String dateFrom,
                        String dateTo);

        @Query("SELECT" +
                        " new com.project.nhatrotot.model.statistics.TotalHousesCreatedStatisticsByMonth(YEAR(h.createdDate), MONTH(h.createdDate), COUNT(h.id)) "
                        +
                        "FROM House h " +
                        "WHERE DATE(h.createdDate) between DATE(?1) and DATE(?2) AND h.houseType = ?3 AND h.houseCategory = ?4 "
                        +
                        "GROUP BY YEAR(h.createdDate), MONTH(h.createdDate) ORDER BY YEAR(h.createdDate) DESC, MONTH(h.createdDate) DESC")
        List<TotalHousesCreatedStatisticsByMonth> findTotalHousesCreatedStatisticsByMonthWithTypeAndCategory(
                        String dateFrom, String dateTo, HouseType type, HouseCategory category);
}
