package com.jpcchaves.authservice.core.strategy;

import com.jpcchaves.authservice.core.enums.ESagaStatus;
import com.jpcchaves.authservice.core.model.Event;
import com.jpcchaves.authservice.core.model.User;
import com.jpcchaves.authservice.core.service.UserService;
import com.jpcchaves.authservice.core.util.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EndRegistrationFailStrategy extends AbstractEndRegistrationSagaStrategy {

    private static final Logger log = LoggerFactory.getLogger(EndRegistrationFailStrategy.class);

    private final UserService userService;
    private final JsonUtil jsonUtil;

    public EndRegistrationFailStrategy(UserService userService, JsonUtil jsonUtil) {
        super(ESagaStatus.FAIL);
        this.userService = userService;
        this.jsonUtil = jsonUtil;
    }

    @Override
    public void handleEvent(Event event) {
        log.info("Received email send fail and set isEmailVerified as false");
        User user = jsonUtil.fromJson(event.getPayload(), User.class);
        userService.isEmailVerified(user, false);
    }
}
