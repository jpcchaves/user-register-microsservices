package com.jpcchaves.orchestrator.core.dto;

import com.jpcchaves.orchestrator.core.enums.EEventSource;
import com.jpcchaves.orchestrator.core.enums.ESagaStatus;

import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class EventDTO<T> {

    private UUID id;
    private String transactionId;
    private T payload;
    private EEventSource source;
    private ESagaStatus status;
    private List<HistoryDTO> eventHistory;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;

    public EventDTO() {}

    public EventDTO(
            UUID id,
            String transactionId,
            T payload,
            EEventSource source,
            ESagaStatus status,
            List<HistoryDTO> eventHistory,
            LocalDateTime createdAt,
            LocalDateTime finishedAt) {
        this.id = id;
        this.transactionId = transactionId;
        this.payload = payload;
        this.source = source;
        this.status = status;
        this.eventHistory = eventHistory;
        this.createdAt = createdAt;
        this.finishedAt = finishedAt;
    }

    public EventDTO(Builder<T> builder) {
        this.id = builder.id;
        this.transactionId = builder.transactionId;
        this.payload = builder.payload;
        this.source = builder.source;
        this.status = builder.status;
        this.eventHistory = builder.eventHistory;
        this.createdAt = builder.createdAt;
        this.finishedAt = builder.finishedAt;
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public EEventSource getSource() {
        return source;
    }

    public void setSource(EEventSource source) {
        this.source = source;
    }

    public ESagaStatus getStatus() {
        return status;
    }

    public void setStatus(ESagaStatus status) {
        this.status = status;
    }

    public List<HistoryDTO> getEventHistory() {
        return eventHistory;
    }

    public void setEventHistory(List<HistoryDTO> eventHistory) {
        this.eventHistory = eventHistory;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    public void addToHistory(HistoryDTO history) {
        if (ObjectUtils.isEmpty(eventHistory)) {
            eventHistory = new ArrayList<>();
        }

        eventHistory.add(history);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EventDTO<?> eventDTO = (EventDTO<?>) o;
        return Objects.equals(transactionId, eventDTO.transactionId)
                && source == eventDTO.source
                && status == eventDTO.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, source, status);
    }

    public static class Builder<T> {
        private UUID id;
        private String transactionId;
        private T payload;
        private EEventSource source;
        private ESagaStatus status;
        private List<HistoryDTO> eventHistory;
        private LocalDateTime createdAt;
        private LocalDateTime finishedAt;

        public Builder<T> id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder<T> transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public Builder<T> payload(T payload) {
            this.payload = payload;
            return this;
        }

        public Builder<T> source(EEventSource source) {
            this.source = source;
            return this;
        }

        public Builder<T> status(ESagaStatus status) {
            this.status = status;
            return this;
        }

        public Builder<T> eventHistory(List<HistoryDTO> eventHistory) {
            this.eventHistory = eventHistory;
            return this;
        }

        public Builder<T> createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder<T> finishedAt(LocalDateTime finishedAt) {
            this.finishedAt = finishedAt;
            return this;
        }

        public EventDTO<T> build() {
            return new EventDTO<>(this);
        }
    }
}
