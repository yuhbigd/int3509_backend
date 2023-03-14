package com.project.nhatrotot.model.statistics;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
@Data
public class PurchaseStatisticsByDate {
    private Date date;
    private BigDecimal total;
    private Long count;

    public PurchaseStatisticsByDate(Date date, BigDecimal total, Long count) {
        this.date = date;
        this.total = total;
        this.count = count;
    }
}
