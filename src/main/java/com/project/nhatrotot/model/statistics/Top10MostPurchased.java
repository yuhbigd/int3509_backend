package com.project.nhatrotot.model.statistics;

import java.math.BigDecimal;

public class Top10MostPurchased {
    private Long count;
    private BigDecimal total;
    private String userId;
    private String email;

    public Top10MostPurchased(Long count, BigDecimal total, String userId, String email) {
        this.count = count;
        this.total = total;
        this.userId = userId;
        this.email = email;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Top10MostPurchased [count=" + count + ", total=" + total + ", userId=" + userId + ", email=" + email
                + "]";
    }
}
