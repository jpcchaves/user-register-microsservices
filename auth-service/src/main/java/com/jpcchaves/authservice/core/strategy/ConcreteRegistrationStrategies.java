package com.jpcchaves.authservice.core.strategy;

import com.jpcchaves.authservice.core.enums.ESagaStatus;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ConcreteRegistrationStrategies {

    private final Map<ESagaStatus, AbstractEndRegistrationSagaStrategy> strategiesMap;

    public ConcreteRegistrationStrategies(List<AbstractEndRegistrationSagaStrategy> strategies) {
        this.strategiesMap =
                strategies.stream()
                        .collect(
                                Collectors.toMap(
                                        AbstractEndRegistrationSagaStrategy::getStrategyName,
                                        Function.identity()));
    }

    public AbstractEndRegistrationSagaStrategy getStrategy(String endSagaStatus) {
        ESagaStatus esagaStatus = ESagaStatus.valueOf(endSagaStatus);
        AbstractEndRegistrationSagaStrategy strategy = strategiesMap.get(esagaStatus);

        if (strategy == null) {
            throw new IllegalArgumentException("Unknown end saga status: " + endSagaStatus);
        }

        return strategiesMap.get(esagaStatus);
    }
}
