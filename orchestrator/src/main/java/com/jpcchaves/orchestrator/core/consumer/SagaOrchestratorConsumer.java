package com.jpcchaves.orchestrator.core.consumer;

import com.jpcchaves.orchestrator.core.dto.EventDTO;
import com.jpcchaves.orchestrator.core.producer.SagaOrchestratorProducer;
import com.jpcchaves.orchestrator.core.service.OrchestratorService;
import com.jpcchaves.orchestrator.core.util.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SagaOrchestratorConsumer {
    private static final Logger logger = LoggerFactory.getLogger(SagaOrchestratorConsumer.class);

    private final JsonUtil jsonUtil;
    private final OrchestratorService orchestratorService;
    private final SagaOrchestratorProducer orchestratorProducer;

    public SagaOrchestratorConsumer(
            JsonUtil jsonUtil,
            OrchestratorService orchestratorService,
            SagaOrchestratorProducer orchestratorProducer) {
        this.jsonUtil = jsonUtil;
        this.orchestratorService = orchestratorService;
        this.orchestratorProducer = orchestratorProducer;
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = {"${spring.kafka.topic.registration-initiated}"})
    public void consumeRegistrationInitiatedTopic(String payload) {

        logger.info("Receiving event {} from registration initiated topic", payload);

        EventDTO<?> event = jsonUtil.toEvent(payload);

        orchestratorService.sendRegistrationEmail(event);
    }
}
