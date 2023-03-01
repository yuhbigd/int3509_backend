package com.project.nhatrotot.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@Table(name = "vnpay_payments")
public class VnPayment {
    @Id
    @Column(name = "vnp_trans_no")
    private String id;
    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinColumn(name = "payment_id", referencedColumnName = "payment_id")
    private Payment payment;
    @Column(name = "vnp_order_info")
    private String vnpOrderInfo;
    @Column(name = "vnp_amount")
    private BigDecimal vnpAmount;
    @Column(name = "vnp_bank_code")
    private String vnpBankCode;
    @Column(name = "vnp_card_type")
    private String vnpCardType;
    @Column(name = "vnp_bank_tran_no")
    private String vnpBankTransNo;
    @Column(name = "vnp_date")
    private LocalDateTime vnpDate;
    @Column(name = "vnp_transaction_status")
    private String status;
}
