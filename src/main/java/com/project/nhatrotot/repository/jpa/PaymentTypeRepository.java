package com.project.nhatrotot.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.nhatrotot.model.PaymentType;


public interface PaymentTypeRepository extends JpaRepository<PaymentType, Integer> {
  
}
