package com.project.nhatrotot.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.nhatrotot.model.House;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface HouseRepository extends JpaRepository<House, Integer> {
    Page<House> findByOwner_UserIdEquals(String userId, Pageable pageable);
}
