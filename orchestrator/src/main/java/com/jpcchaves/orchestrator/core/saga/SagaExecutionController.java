package com.jpcchaves.orchestrator.core.saga;

import static com.jpcchaves.orchestrator.core.enums.ESagaStatus.FAIL;
import static com.jpcchaves.orchestrator.core.enums.ESagaStatus.SUCCESS;
import static com.jpcchaves.orchestrator.core.saga.SagaHandler.EVENT_SOURCE_INDEX;
import static com.jpcchaves.orchestrator.core.saga.SagaHandler.SAGA_HANDLER;
import static com.jpcchaves.orchestrator.core.saga.SagaHandler.SAGA_STATUS_INDEX;
import static com.jpcchaves.orchestrator.core.saga.SagaHandler.TOPIC_INDEX;

import com.jpcchaves.orchestrator.core.dto.EventDTO;
import com.jpcchaves.orchestrator.core.enums.EEventSource;
import com.jpcchaves.orchestrator.core.enums.ETopics;
import com.jpcchaves.orchestrator.core.exception.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;

@Component
public class SagaExecutionController {

    private static final Logger log = LoggerFactory.getLogger(SagaExecutionController.class);

    private static final String SAGA_LOG_ID = "TRANSACTION_ID: %s | EVENT_ID: %s";
    private static final String SUCCESS_LOG_MESSAGE =
            "### CURRENT SAGA: {} | SUCCESS | NEXT TOPIC: {} | {}";
    private static final String FAIL_LOG_MESSAGE =
            "### CURRENT SAGA: {} | FAIL SENDING TO ROLLBACK PREVIOUS SERVICE | NEXT TOPIC: {} | {}";

    public ETopics getNextTopic(EventDTO<?> event) {

        if (ObjectUtils.isEmpty(event.getStatus()) || ObjectUtils.isEmpty(event.getSource())) {
            throw new ValidationException("Source and status must be informed!");
        }

        ETopics topic = findTopicBySourceAndStatus(event);

        logCurrentSaga(event, topic);

        return topic;
    }

    private ETopics findTopicBySourceAndStatus(EventDTO<?> event) {
        return (ETopics)
                (Arrays.stream(SAGA_HANDLER)
                        .filter(row -> isEventSourceAndStatusValid(event, row))
                        .map(i -> i[TOPIC_INDEX])
                        .findFirst()
                        .orElseThrow(() -> new ValidationException("Topic not found!")));
    }

    private boolean isEventSourceAndStatusValid(EventDTO<?> event, Object[] row) {
        Object source = row[EVENT_SOURCE_INDEX];
        Object status = row[SAGA_STATUS_INDEX];

        return event.getSource().equals(source) && event.getStatus().equals(status);
    }

    private void logCurrentSaga(EventDTO<?> event, ETopics topic) {
        String sagaId = createSagaId(event);
        EEventSource source = event.getSource();

        switch (event.getStatus()) {
            case SUCCESS -> log.info(SUCCESS_LOG_MESSAGE, source, topic, sagaId);
            case FAIL -> log.info(FAIL_LOG_MESSAGE, source, topic, sagaId);
        }
    }

    private String createSagaId(EventDTO<?> event) {
        return String.format(SAGA_LOG_ID, event.getTransactionId(), event.getId());
    }
}
