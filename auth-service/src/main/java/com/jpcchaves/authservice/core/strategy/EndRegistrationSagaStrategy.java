package com.jpcchaves.authservice.core.strategy;

import com.jpcchaves.authservice.core.enums.ESagaStatus;
import com.jpcchaves.authservice.core.model.Event;

public abstract class EndRegistrationSagaStrategy {
    ESagaStatus strategyName;

    public abstract void handleEvent(Event event);

    public EndRegistrationSagaStrategy(ESagaStatus strategyName) {
        this.strategyName = strategyName;
    }
}
