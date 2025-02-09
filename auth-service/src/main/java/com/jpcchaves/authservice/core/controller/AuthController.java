package com.jpcchaves.authservice.core.controller;

import com.jpcchaves.authservice.core.model.dto.UserRegisterDTO;
import com.jpcchaves.authservice.core.model.dto.UserResponseDTO;
import com.jpcchaves.authservice.core.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(
            @Valid @RequestBody UserRegisterDTO requestBody) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(requestBody));
    }
}
