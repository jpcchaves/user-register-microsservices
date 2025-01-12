package com.jpcchaves.orchestrator.core.dto;

import com.jpcchaves.orchestrator.core.enums.EEventSource;
import com.jpcchaves.orchestrator.core.enums.ESagaStatus;

import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventDTO {

    private String id;
    private String transactionId;
    private Object payload;
    private EEventSource source;
    private ESagaStatus status;
    private List<HistoryDTO> eventHistory;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;

    public EventDTO() {}

    public EventDTO(
            String id,
            String transactionId,
            Object payload,
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

    public EventDTO(Builder builder) {
        this.id = builder.id;
        this.transactionId = builder.transactionId;
        this.payload = builder.payload;
        this.source = builder.source;
        this.status = builder.status;
        this.eventHistory = builder.eventHistory;
        this.createdAt = builder.createdAt;
        this.finishedAt = builder.finishedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
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
    }

    public static class Builder {
        private String id;
        private String transactionId;
        private Object payload;
        private EEventSource source;
        private ESagaStatus status;
        private List<HistoryDTO> eventHistory;
        private LocalDateTime createdAt;
        private LocalDateTime finishedAt;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public Builder payload(Object payload) {
            this.payload = payload;
            return this;
        }

        public Builder source(EEventSource source) {
            this.source = source;
            return this;
        }

        public Builder status(ESagaStatus status) {
            this.status = status;
            return this;
        }

        public Builder eventHistory(List<HistoryDTO> eventHistory) {
            this.eventHistory = eventHistory;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder finishedAt(LocalDateTime finishedAt) {
            this.finishedAt = finishedAt;
            return this;
        }

        public EventDTO build() {
            return new EventDTO(this);
        }
    }
}
