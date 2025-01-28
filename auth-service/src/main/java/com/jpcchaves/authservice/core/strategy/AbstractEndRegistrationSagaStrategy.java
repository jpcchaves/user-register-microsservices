package com.jpcchaves.authservice.core.strategy;

import com.jpcchaves.authservice.core.enums.ESagaStatus;
import com.jpcchaves.authservice.core.model.Event;

public abstract class AbstractEndRegistrationSagaStrategy {
    private final ESagaStatus strategyName;

    protected AbstractEndRegistrationSagaStrategy(ESagaStatus strategyName) {
        this.strategyName = strategyName;
    }

    public ESagaStatus getStrategyName() {
        return strategyName;
    }

    public abstract void handleEvent(Event event);
}
