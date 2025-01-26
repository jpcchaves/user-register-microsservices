package com.jpcchaves.authservice.core.strategy;

import com.jpcchaves.authservice.core.enums.ESagaStatus;
import com.jpcchaves.authservice.core.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EndRegistrationSuccessStrategy extends AbstractEndRegistrationSagaStrategy {

    private static final Logger log = LoggerFactory.getLogger(EndRegistrationSuccessStrategy.class);


    EndRegistrationSuccessStrategy() {
        super(ESagaStatus.SUCCESS);
    }

    @Override
    public void handleEvent(Event event) {
        log.info("Received event in success strategy {}", event);
    }
}
