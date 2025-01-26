package com.jpcchaves.authservice.core.service;

import com.jpcchaves.authservice.core.exception.PasswordsMissmatchException;
import com.jpcchaves.authservice.core.model.Event;
import com.jpcchaves.authservice.core.model.User;
import com.jpcchaves.authservice.core.model.dto.UserRegisterDTO;
import com.jpcchaves.authservice.core.model.dto.UserResponseDTO;
import com.jpcchaves.authservice.core.producer.SagaProducer;
import com.jpcchaves.authservice.core.repository.UserRepository;
import com.jpcchaves.authservice.core.util.JsonUtil;
import com.jpcchaves.authservice.core.util.TransactionHelper;
import com.jpcchaves.authservice.core.util.UserMapper;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final JsonUtil jsonUtil;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final SagaProducer sagaProducer;
    private final TransactionHelper transactionHelper;

    public AuthService(
            JsonUtil jsonUtil,
            UserMapper userMapper,
            UserRepository userRepository,
            SagaProducer sagaProducer,
            TransactionHelper transactionHelper) {
        this.jsonUtil = jsonUtil;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.sagaProducer = sagaProducer;
        this.transactionHelper = transactionHelper;
    }

    public UserResponseDTO register(UserRegisterDTO requestBody) {
        log.info("Registering user...");

        User user = userMapper.toUser(requestBody);
        validatePassword(requestBody);
        user.setPassword(encodePassword(requestBody.getPassword()));
        userRepository.save(user);

        sagaProducer.sendEvent(jsonUtil.toJson(createPayload()));

        return UserResponseDTO.builder()
                .id(Long.valueOf(UUID.randomUUID().toString()))
                .email(requestBody.getEmail())
                .firstName(requestBody.getFirstName())
                .lastName(requestBody.getLastName())
                .phoneNumber(requestBody.getPhoneNumber())
                .build();
    }

    private String encodePassword(String password) {
        return DigestUtils.sha1Hex(password.getBytes());
    }

    private void validatePassword(UserRegisterDTO requestBody) {
        if (!ObjectUtils.nullSafeEquals(
                requestBody.getPassword(), requestBody.getConfirmPassword())) {
            throw new PasswordsMissmatchException("Passwords must match!");
        }
    }

    private Event createPayload() {
        return Event.builder()
                .transactionId(transactionHelper.generateTransactionId())
                .payload("Teste")
                .createdAt(LocalDateTime.now())
                .build();
    }
}
