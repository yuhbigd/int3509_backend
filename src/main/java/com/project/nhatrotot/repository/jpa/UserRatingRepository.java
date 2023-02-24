package com.project.nhatrotot.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.nhatrotot.model.UserRating;

public interface UserRatingRepository extends JpaRepository<UserRating, Long> {
    Optional<UserRating> findByUser_userIdAndFromId(String userId, String fromId);
}
