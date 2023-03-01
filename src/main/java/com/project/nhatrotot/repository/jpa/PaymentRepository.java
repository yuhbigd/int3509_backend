package com.project.nhatrotot.repository.jpa;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.nhatrotot.model.Payment;
import org.springframework.data.domain.Page;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    Page<Payment> findByType_IdEqualsAndUser_UserIdEquals(Integer id, String userId, Pageable pageable);

    Page<Payment> findByUser_UserIdEquals(String userId, Pageable pageable);

    Page<Payment> findByUser_EmailContainingAndPaymentStateContainingAllIgnoreCase(String userEmail,
            String paymentStatus, Pageable pageable);

    Page<Payment> findByType_IdEqualsAndUser_EmailContainingAndPaymentStateContainingAllIgnoreCase(Integer id,
            String userEmail, String paymentStatus, Pageable pageable);
}
