package com.project.nhatrotot.repository.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.nhatrotot.model.FollowingEntity;
import com.project.nhatrotot.model.FollowingKey;

@Repository
public interface FollowingRepository extends JpaRepository<FollowingEntity, FollowingKey> {
    Page<FollowingEntity> findById_UserId(String userId, Pageable pageable);
    Page<FollowingEntity> findById_FollowingId(String userId, Pageable pageable);
}
