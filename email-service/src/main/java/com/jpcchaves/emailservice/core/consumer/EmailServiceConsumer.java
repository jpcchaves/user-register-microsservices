package com.jpcchaves.emailservice.core.consumer;

import com.jpcchaves.emailservice.core.dto.EventDTO;
import com.jpcchaves.emailservice.core.service.EmailService;
import com.jpcchaves.emailservice.core.util.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceConsumer {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceConsumer.class);
    private final EmailService emailService;
    private final JsonUtil jsonUtil;

    public EmailServiceConsumer(EmailService emailService, JsonUtil jsonUtil) {
        this.emailService = emailService;
        this.jsonUtil = jsonUtil;
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = {"${spring.kafka.topic.email-success}"})
    public void consumerEmailSuccessTopic(String payload) {
        log.info("Receiving event {} from email-success topic", payload);
        EventDTO<?> event = jsonUtil.toEvent(payload);
        emailService.sendEmail(event);
    }
}
