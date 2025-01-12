package com.jpcchaves.authservice.core.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SagaProducer {

    private static final Logger log = LoggerFactory.getLogger(SagaProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topic.registration-initiated}")
    private String registrationInitiatedTopic;

    public SagaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(String payload) {
        try {
            log.info("Sending event to topic {} with data {}", registrationInitiatedTopic, payload);

            kafkaTemplate.send(registrationInitiatedTopic, payload);
        } catch (Exception e) {
            log.error(
                    "Error trying to send data to topic {} with data {}",
                    registrationInitiatedTopic,
                    payload,
                    e);
        }
    }
}
