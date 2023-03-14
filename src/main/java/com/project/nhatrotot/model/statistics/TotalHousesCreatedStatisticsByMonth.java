package com.project.nhatrotot.model.statistics;

import lombok.ToString;

@ToString
public class TotalHousesCreatedStatisticsByMonth {
    private Integer year;
    private Integer month;
    private Long count;

    public TotalHousesCreatedStatisticsByMonth(Integer year, Integer month, Long count) {
        this.year = year;
        this.month = month;
        this.count = count;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
