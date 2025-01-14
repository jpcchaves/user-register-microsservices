package com.jpcchaves.authservice.core.consumer;

import com.jpcchaves.authservice.core.model.Event;
import com.jpcchaves.authservice.core.service.EventService;
import com.jpcchaves.authservice.core.util.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EventConsumer {

    private static final Logger log = LoggerFactory.getLogger(EventConsumer.class);
    private final EventService eventService;
    private final JsonUtil jsonUtil;

    public EventConsumer(EventService eventService, JsonUtil jsonUtil) {
        this.eventService = eventService;
        this.jsonUtil = jsonUtil;
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = {"${spring.kafka.topic.registration-completed}"})
    public void consumeRegistrationCompletedTopic(String payload) {
        log.info("Received registration completed topic: {}", payload);
        Event event = jsonUtil.toEvent(payload);
        eventService.endRegistrationSaga(event);
    }
}
