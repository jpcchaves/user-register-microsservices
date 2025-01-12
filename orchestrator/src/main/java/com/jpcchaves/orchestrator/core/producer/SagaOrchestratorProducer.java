package com.jpcchaves.orchestrator.core.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SagaOrchestratorProducer {
    private static final Logger logger = LoggerFactory.getLogger(SagaOrchestratorProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    public SagaOrchestratorProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(String topic, String payload) {
        try {

            logger.info("Sending event to topic {} with data {}", topic, payload);
            kafkaTemplate.send(topic, payload);
        } catch (Exception e) {
            logger.error("Error trying to send data to topic {} with data {}", topic, payload, e);
        }
    }
}
