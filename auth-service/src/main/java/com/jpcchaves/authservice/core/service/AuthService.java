package com.jpcchaves.authservice.core.service;

import com.jpcchaves.authservice.core.exception.PasswordsMissmatchException;
import com.jpcchaves.authservice.core.model.Event;
import com.jpcchaves.authservice.core.model.Role;
import com.jpcchaves.authservice.core.model.User;
import com.jpcchaves.authservice.core.model.dto.UserRegisterDTO;
import com.jpcchaves.authservice.core.model.dto.UserResponseDTO;
import com.jpcchaves.authservice.core.producer.SagaProducer;
import com.jpcchaves.authservice.core.repository.RoleRepository;
import com.jpcchaves.authservice.core.repository.UserRepository;
import com.jpcchaves.authservice.core.strategy.ConcreteRegistrationStrategies;
import com.jpcchaves.authservice.core.strategy.EndRegistrationSagaStrategy;
import com.jpcchaves.authservice.core.util.JsonUtil;
import com.jpcchaves.authservice.core.util.TransactionHelper;
import com.jpcchaves.authservice.core.util.UserMapper;
import java.time.LocalDateTime;
import java.util.Set;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final JsonUtil jsonUtil;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SagaProducer sagaProducer;
    private final TransactionHelper transactionHelper;
    private final ConcreteRegistrationStrategies registrationStrategies;

    public AuthService(
            JsonUtil jsonUtil,
            UserMapper userMapper,
            UserRepository userRepository, RoleRepository roleRepository,
            SagaProducer sagaProducer,
            TransactionHelper transactionHelper, ConcreteRegistrationStrategies registrationStrategies) {
        this.jsonUtil = jsonUtil;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.sagaProducer = sagaProducer;
        this.transactionHelper = transactionHelper;
        this.registrationStrategies = registrationStrategies;
    }

    public UserResponseDTO register(UserRegisterDTO requestBody) {
        log.info("Registering user...");

        User user = userMapper.toUser(requestBody);
        validatePassword(requestBody);
        user.setPassword(encodePassword(requestBody.getPassword()));
        user.setRoles(Set.of(getTestRole()));
        userRepository.save(user);

        sagaProducer.sendEvent(jsonUtil.toJson(createPayload(user)));

        return UserResponseDTO.builder()
                .id(user.getId())
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

    private Event createPayload(User user) {
        return Event.builder()
                .transactionId(transactionHelper.generateTransactionId())
                .payload(jsonUtil.toJson(user))
                .createdAt(LocalDateTime.now())
                .build();
    }

    private Role getTestRole() {
        String TEST_ROLE = "TEST_ROLE";
        return roleRepository.findByName(TEST_ROLE)
                .orElseGet(() -> roleRepository.save(new Role(TEST_ROLE)));
    }

    public void handleEndRegistrationSaga(Event event) {
        EndRegistrationSagaStrategy strategy = registrationStrategies.getStrategy(event.getStatus());
        strategy.handleEvent(event);
    }
}
