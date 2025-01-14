package com.jpcchaves.orchestrator.core.service;

import com.jpcchaves.orchestrator.core.dto.EventDTO;
import com.jpcchaves.orchestrator.core.dto.HistoryDTO;
import com.jpcchaves.orchestrator.core.enums.EEventSource;
import com.jpcchaves.orchestrator.core.enums.ESagaStatus;
import com.jpcchaves.orchestrator.core.enums.ETopics;
import com.jpcchaves.orchestrator.core.producer.SagaOrchestratorProducer;
import com.jpcchaves.orchestrator.core.saga.SagaExecutionController;
import com.jpcchaves.orchestrator.core.util.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrchestratorService {

    private static final Logger log = LoggerFactory.getLogger(OrchestratorService.class);

    private final SagaOrchestratorProducer sagaOrchestratorProducer;
    private final SagaExecutionController sagaExecutionController;
    private final JsonUtil jsonUtil;

    public OrchestratorService(
            SagaExecutionController sagaExecutionController,
            JsonUtil jsonUtil,
            SagaOrchestratorProducer sagaOrchestratorProducer) {
        this.sagaExecutionController = sagaExecutionController;
        this.jsonUtil = jsonUtil;
        this.sagaOrchestratorProducer = sagaOrchestratorProducer;
    }

    public void sendRegistrationEmail(EventDTO<?> event) {
        event.setSource(EEventSource.ORCHESTRATOR);
        event.setStatus(ESagaStatus.SUCCESS);

        // it will get email-success topic and send the event to email-service
        ETopics topic = getTopic(event);

        log.info("STARTED REGISTRATION EMAIL SAGA!");
        addHistory(event, "Started registration email saga!");
        produceEvent(event, topic);
    }

    private ETopics getTopic(EventDTO<?> event) {
        return sagaExecutionController.getNextTopic(event);
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

    private void produceEvent(EventDTO<?> event, ETopics topic) {
        sagaOrchestratorProducer.sendEvent(topic.getTopic(), jsonUtil.toJson(event));
    }

    public void finishRegistrationSaga(EventDTO<?> event) {
        event.setSource(EEventSource.ORCHESTRATOR);

        log.info(
                "FINISHED REGISTRATION SAGA WITH STATUS {} FOR EVENT: {}!",
                event.getStatus(),
                event.getId());

        addHistory(event, "Finished registration saga!");

        produceEvent(event, ETopics.REGISTRATION_COMPLETED);
    }

    public void continueSaga(EventDTO<?> event) {
        ETopics topic = sagaExecutionController.getNextTopic(event);
        log.info(
                "CONTINUING SAGA FOR EVENT {}, GOING TO NEXT TOPIC {}",
                event.getId(),
                topic.getTopic());
        sagaOrchestratorProducer.sendEvent(topic.getTopic(), jsonUtil.toJson(event));
    }
}
