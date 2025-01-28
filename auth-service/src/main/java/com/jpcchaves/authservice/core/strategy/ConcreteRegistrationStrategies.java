package com.jpcchaves.authservice.core.strategy;

import com.jpcchaves.authservice.core.enums.ESagaStatus;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ConcreteRegistrationStrategies {

    public Map<ESagaStatus, AbstractEndRegistrationSagaStrategy> strategiesMap = new HashMap<>();

    public ConcreteRegistrationStrategies(List<AbstractEndRegistrationSagaStrategy> strategies) {
        strategies.forEach(strategy -> strategiesMap.put(strategy.getStrategyName(), strategy));
    }

    public AbstractEndRegistrationSagaStrategy getStrategy(String endSagaStatus) {
        ESagaStatus esagaStatus = ESagaStatus.valueOf(endSagaStatus);
        return strategiesMap.get(esagaStatus);
    }
}
