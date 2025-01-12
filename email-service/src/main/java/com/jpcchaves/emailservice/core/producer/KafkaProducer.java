package com.jpcchaves.emailservice.core.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topic.orchestrator}")
    private String orchestratorTopic;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(String payload) {
        try {

            log.info("Sending event to topic {} with data {}", orchestratorTopic, payload);

            kafkaTemplate.send(orchestratorTopic, payload);
        } catch (Exception e) {
            log.error(
                    "Error trying to send event to topic {} with data {}",
                    orchestratorTopic,
                    payload,
                    e);
        }
    }
}
