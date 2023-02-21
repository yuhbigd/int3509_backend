package com.project.nhatrotot.service.db_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.nhatrotot.model.UserEntity;
import com.project.nhatrotot.repository.jpa.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void saveUser(UserEntity userEntity) {
        userRepository.save(userEntity);
    }
}
