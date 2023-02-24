package com.project.nhatrotot.repository.jpa;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import com.project.nhatrotot.model.Gender;
import com.project.nhatrotot.model.UserEntity;
import com.project.nhatrotot.model.UserTitle;

public interface UserEntityRepository extends JpaRepository<UserEntity, String> {
    @Modifying
    @Query("update UserEntity u set u.banned =?2 where u.userId = ?1")
    void deactivatedUsers(String userId, boolean banned);

    @Modifying
    @Query("update UserEntity u set u.gender =?2, u.phoneNumber = ?3, u.intro = ?4, u.image = ?5, u.birthDate = ?6 where u.userId = ?1")
    void updateInformation(String userId, Gender gender, String phoneNumber, String intro, String image,
            LocalDateTime birthDate);

    @Modifying
    @Query("update UserEntity u set u.title =?2 where u.userId = ?1")
    void updateTitle(String userId, UserTitle title);

    Page<UserEntity> findByEmailContainingIgnoreCase(String email, Pageable pageable);

    Page<UserEntity> findByEmailContainingIgnoreCaseAndRole_id(String email, Long roleId, Pageable pageable);
}
