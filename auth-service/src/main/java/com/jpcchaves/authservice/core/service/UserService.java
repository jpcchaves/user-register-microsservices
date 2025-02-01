package com.jpcchaves.authservice.core.service;

import com.jpcchaves.authservice.core.model.User;
import com.jpcchaves.authservice.core.repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void isEmailVerified(User user, Boolean emailVerified) {
        user.setEmailVerified(emailVerified);
        userRepository.save(user);
    }
}
