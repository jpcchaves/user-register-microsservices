package com.jpcchaves.emailservice.core.service;

import com.jpcchaves.emailservice.core.dto.EventDTO;
import com.jpcchaves.emailservice.core.dto.HistoryDTO;
import com.jpcchaves.emailservice.core.enums.ESagaStatus;
import com.jpcchaves.emailservice.core.model.EmailRequests;
import com.jpcchaves.emailservice.core.repository.EmailRequestsRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService {

    private static final String CURRENT_SOURCE = "EMAIL_SERVICE";

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final EmailRequestsRepository emailRequestsRepository;

    public EmailService(EmailRequestsRepository emailRequestsRepository) {
        this.emailRequestsRepository = emailRequestsRepository;
    }

    public void sendEmail(EventDTO<?> event) {
        try {
            log.info("Sending email. Event received: {}", event);
            verifyDuplicateEvent(event);
            createEmailRequests(event, Boolean.TRUE);
            handleSuccess(event);
        } catch (Exception e) {
            log.error("Error sending email", e);
            handleFailure(event, e.getMessage());
        }
    }

    private void handleFailure(EventDTO<?> event, String message) {
        event.setStatus(ESagaStatus.FAIL);
        event.setSource(CURRENT_SOURCE);
        addHistory(event, "Email service failed: " + message);
    }

    private void verifyDuplicateEvent(EventDTO<?> event) {
        if (existsByIdAndTransactionId(event)) {
            throw new RuntimeException("There is already an event with this transaction id");
        }
    }

    private boolean existsByIdAndTransactionId(EventDTO<?> event) {
        return emailRequestsRepository.existsByTransactionId(event.getTransactionId());
    }

    private void createEmailRequests(EventDTO<?> event, boolean success) {
        EmailRequests emailRequests =
                EmailRequests.builder()
                        .transactionId(event.getTransactionId())
                        .success(success)
                        .build();

        emailRequestsRepository.save(emailRequests);
    }

    private void handleSuccess(EventDTO<?> event) {
        event.setStatus(ESagaStatus.SUCCESS);
        event.setSource(CURRENT_SOURCE);

        addHistory(event, "Email sent successfully!");
    }

    private void addHistory(EventDTO<?> event, String message) {
        HistoryDTO history =
                HistoryDTO.builder()
                        .source(event.getSource())
                        .status(event.getStatus())
                        .message(message)
                        .createdAt(LocalDateTime.now())
                        .build();

        event.addToHistory(history);
    }
}
