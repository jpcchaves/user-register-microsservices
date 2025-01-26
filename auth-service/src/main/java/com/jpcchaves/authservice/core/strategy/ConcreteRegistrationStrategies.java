package com.jpcchaves.authservice.core.strategy;

import com.jpcchaves.authservice.core.enums.ESagaStatus;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ConcreteRegistrationStrategies {

    public Map<ESagaStatus, AbstractEndRegistrationSagaStrategy> strategiesMap = new HashMap<>();

    public ConcreteRegistrationStrategies(List<AbstractEndRegistrationSagaStrategy> strategies) {
        strategies.forEach(strategy -> strategiesMap.put(strategy.strategyName, strategy));
    }

    public AbstractEndRegistrationSagaStrategy getStrategy(String endSagaStatus) {
        ESagaStatus esagaStatus = ESagaStatus.valueOf(endSagaStatus);
        return strategiesMap.get(esagaStatus);
    }
}
