package com.project.nhatrotot.model.statistics;

import java.util.Date;

import lombok.ToString;

@ToString
public class TotalHousesCreatedStatisticsByDate {
    private Date date;
    private Long count;

    public TotalHousesCreatedStatisticsByDate(Date date, Long count) {
        this.date = date;
        this.count = count;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
