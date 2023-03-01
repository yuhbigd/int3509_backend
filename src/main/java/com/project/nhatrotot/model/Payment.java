package com.project.nhatrotot.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "payments")
public class Payment {
    @Id
    @Column(name = "payment_id")
    @EqualsAndHashCode.Include
    private String paymentId;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    private BigDecimal total;
    @Column(name = "payment_description")
    private String paymentDescription;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ToString.Exclude
    private UserEntity user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type", referencedColumnName = "id")
    private PaymentType type;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private PaymentState paymentState;
    @OneToOne(mappedBy = "payment", fetch = FetchType.LAZY, optional = true)
    @ToString.Exclude
    private VnPayment vnPayment;
    @OneToOne(mappedBy = "payment", fetch = FetchType.LAZY, optional = true)
    @ToString.Exclude
    private ManualPayment manualPayment;
}
