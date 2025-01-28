package com.jpcchaves.authservice.core.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, updatable = false)
    private String transactionId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    private String source;

    private String status;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(
            name = "event_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "event_fk", value = ConstraintMode.CONSTRAINT))
    private List<History> eventHistory;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime finishedAt;

    public Event() {}

    public Event(
            UUID id,
            String transactionId,
            String payload,
            String source,
            String status,
            List<History> eventHistory,
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

    public Event(Builder builder) {
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

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<History> getEventHistory() {
        return eventHistory;
    }

    public void setEventHistory(List<History> eventHistory) {
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

    public void addToHistory(History history) {
        if (ObjectUtils.isEmpty(eventHistory)) {
            eventHistory = new ArrayList<>();
        }

        eventHistory.add(history);
    }

    @Override
    public String toString() {
        return "Event{" + "id=" + id + ", transactionId='" + transactionId + '\'' + '}';
    }

    public static class Builder {
        private UUID id;
        private String transactionId;
        private String payload;
        private String source;
        private String status;
        private List<History> eventHistory;
        private LocalDateTime createdAt;
        private LocalDateTime finishedAt;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public Builder payload(String payload) {
            this.payload = payload;
            return this;
        }

        public Builder source(String source) {
            this.source = source;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder eventHistory(List<History> eventHistory) {
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

        public Event build() {
            return new Event(this);
        }
    }
}
