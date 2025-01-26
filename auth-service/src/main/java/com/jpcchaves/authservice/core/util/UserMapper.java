package com.jpcchaves.authservice.core.util;

import com.jpcchaves.authservice.core.model.User;
import com.jpcchaves.authservice.core.model.dto.UserRegisterDTO;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toUser(UserRegisterDTO registerDTO) {
        return User.builder()
                .firstName(registerDTO.getFirstName())
                .lastName(registerDTO.getLastName())
                .email(registerDTO.getEmail())
                .password(registerDTO.getPassword())
                .phoneNumber(registerDTO.getPhoneNumber())
                .build();
    }
}
