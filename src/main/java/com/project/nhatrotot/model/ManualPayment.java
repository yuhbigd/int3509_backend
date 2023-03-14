package com.project.nhatrotot.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.CascadeType;
import org.apache.commons.lang3.builder.ToStringExclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "manual_payments")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ManualPayment {
    @Id
    @Column(name = "payment_id")
    @EqualsAndHashCode.Include
    private String paymentId;
    @OneToOne(cascade = CascadeType.MERGE, optional = false)
    @PrimaryKeyJoinColumn(name = "payment_id", referencedColumnName = "payment_id")
    @ToStringExclude
    private Payment payment;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", referencedColumnName = "user_id")
    UserEntity creator;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verifier_id", referencedColumnName = "user_id")
    UserEntity verifier;
    @Column(name = "total")
    private BigDecimal amount;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
