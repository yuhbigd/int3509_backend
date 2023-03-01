package com.project.nhatrotot.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.nhatrotot.model.VnPayment;

public interface VnpayRespository extends JpaRepository<VnPayment, String> {
    Optional<VnPayment> findByPayment_PaymentIdEquals(String userId);
}
