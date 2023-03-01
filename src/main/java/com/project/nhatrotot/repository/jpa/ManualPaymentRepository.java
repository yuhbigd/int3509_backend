package com.project.nhatrotot.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.nhatrotot.model.ManualPayment;
public interface ManualPaymentRepository extends JpaRepository<ManualPayment, String> {

}
