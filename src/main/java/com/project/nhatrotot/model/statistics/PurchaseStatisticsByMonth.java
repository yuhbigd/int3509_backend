package com.project.nhatrotot.model.statistics;

import java.math.BigDecimal;
import lombok.ToString;

@ToString
public class PurchaseStatisticsByMonth {
    private Integer year;
    private Integer month;
    private BigDecimal total;
    private Long count;

    public PurchaseStatisticsByMonth(Integer year, Integer month, BigDecimal total, Long count) {
        this.year = year;
        this.month = month;
        this.total = total;
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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
