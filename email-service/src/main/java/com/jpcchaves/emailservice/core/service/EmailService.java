package com.jpcchaves.emailservice.core.service;

import com.jpcchaves.emailservice.core.dto.EventDTO;
import com.jpcchaves.emailservice.core.dto.HistoryDTO;
import com.jpcchaves.emailservice.core.dto.UserDTO;
import com.jpcchaves.emailservice.core.enums.EEmailStatus;
import com.jpcchaves.emailservice.core.enums.ESagaStatus;
import com.jpcchaves.emailservice.core.model.EmailRequests;
import com.jpcchaves.emailservice.core.producer.KafkaProducer;
import com.jpcchaves.emailservice.core.repository.EmailRequestsRepository;
import com.jpcchaves.emailservice.core.util.JsonUtil;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService {

    private static final String CURRENT_SOURCE = "EMAIL_SERVICE";

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final EmailRequestsRepository emailRequestsRepository;
    private final KafkaProducer producer;
    private final JsonUtil jsonUtil;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public EmailService(
            EmailRequestsRepository emailRequestsRepository,
            KafkaProducer producer,
            JsonUtil jsonUtil,
            JavaMailSender mailSender) {
        this.emailRequestsRepository = emailRequestsRepository;
        this.producer = producer;
        this.jsonUtil = jsonUtil;
        this.mailSender = mailSender;
    }

    public void sendEmail(EventDTO<?> event) {
        try {
            log.info("Sending email. Event received: {}", event);
            verifyDuplicateEvent(event);
            createEmailRequests(event, EEmailStatus.SUCCESS);
            sendRegistrationEmail(extractUserFromEvent(event));
            handleSuccess(event);
        } catch (Exception e) {
            log.error("Error sending email", e);
            handleFailure(event, e.getMessage());
        }

        producer.sendEvent(jsonUtil.toJson(event));
    }

    public void invalidateEmailRequest(EventDTO<?> event) {
        failEmailRequest(event);
        event.setStatus(ESagaStatus.FAIL);
        event.setSource(CURRENT_SOURCE);

        addHistory(event, "Invalidated email request!");
        addHistory(event, "Saga finished with failure!");

        producer.sendEvent(jsonUtil.toJson(event));
    }

    private void failEmailRequest(EventDTO<?> event) {
        emailRequestsRepository
                .findByTransactionId(event.getTransactionId())
                .ifPresentOrElse(
                        emailRequest -> {
                            emailRequest.setStatus(EEmailStatus.FAIL);
                            emailRequestsRepository.save(emailRequest);
                        },
                        () -> createEmailRequests(event, EEmailStatus.FAIL));
    }

    private void handleFailure(EventDTO<?> event, String message) {
        event.setStatus(ESagaStatus.ROLLBACK_PENDING);
        event.setSource(CURRENT_SOURCE);
        event.setFinishedAt(LocalDateTime.now());
        addHistory(event, "Email service failed: " + message);
    }

    private void verifyDuplicateEvent(EventDTO<?> event) {
        if (existsByTransactionId(event)) {
            throw new RuntimeException("There is already an event with this transaction id");
        }
    }

    private boolean existsByTransactionId(EventDTO<?> event) {
        return emailRequestsRepository.existsByTransactionId(event.getTransactionId());
    }

    private void createEmailRequests(EventDTO<?> event, EEmailStatus status) {
        EmailRequests emailRequests =
                EmailRequests.builder()
                        .transactionId(event.getTransactionId())
                        .status(status)
                        .build();

        emailRequestsRepository.save(emailRequests);
    }

    private void handleSuccess(EventDTO<?> event) {
        event.setStatus(ESagaStatus.SUCCESS);
        event.setSource(CURRENT_SOURCE);
        event.setFinishedAt(LocalDateTime.now());

        addHistory(event, "Email sent successfully!");
        addHistory(event, "Saga finished with success!");
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

    private UserDTO extractUserFromEvent(EventDTO<?> event) {
        return jsonUtil.toUser(event.getPayload());
    }

    private void sendRegistrationEmail(UserDTO user) throws MessagingException {
        mailSender.send(mountEmail(user.getEmail(), user.getFirstName()));
    }

    private MimeMessage mountEmail(String recipientEmail, String recipientName)
            throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        helper.setFrom(from);
        helper.setTo(recipientEmail);
        helper.setSubject("Boas vindas ao nosso sistema, %s!".formatted(recipientName));
        helper.setText(getRegistrationEmailBody(recipientName), true);

        return mimeMessage;
    }

    private String getRegistrationEmailBody(String recipientName) {
        StringBuilder builder = new StringBuilder();

        builder.append("<h1>");
        builder.append("Bem vindo ao nosso sistema, ");
        builder.append(recipientName);
        builder.append("</h1>");

        builder.append("</br>");

        builder.append("<p>");
        builder.append("Estamos felizes em ter você conosco!");
        builder.append("</p>");

        builder.append("<p>");
        builder.append(
                "Caso tenha recebido esse email, significa que seu cadastro foi realizado com sucesso e você já pode desfrutar da nossa plataforma!");
        builder.append("</p>");

        return builder.toString();
    }
}
