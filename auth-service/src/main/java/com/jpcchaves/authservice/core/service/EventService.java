package com.jpcchaves.authservice.core.service;

import com.jpcchaves.authservice.core.model.Event;
import com.jpcchaves.authservice.core.model.History;
import com.jpcchaves.authservice.core.repository.EventRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EventService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void endRegistrationSaga(Event event) {
        event.setFinishedAt(LocalDateTime.now());
        addHistory(event, String.format("Saga finished with status %s", event.getStatus()));

        eventRepository.save(event);

        log.info("Saga with transactionId: {} has been ended", event.getTransactionId());
    }

    private void addHistory(Event event, String message) {
        History history =
                History.builder()
                        .source(event.getSource())
                        .status(event.getStatus())
                        .message(message)
                        .createdAt(LocalDateTime.now())
                        .build();

        event.addToHistory(history);
    }
}
