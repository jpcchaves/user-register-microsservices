package com.jpcchaves.emailservice.core.model;

import com.jpcchaves.emailservice.core.enums.EEmailStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "email_requests")
public class EmailRequests implements Serializable {

    @Serial private static final long serialVersionUID = 3245773646981513804L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String transactionId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EEmailStatus status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime updatedAt;

    public EmailRequests() {}

    public EmailRequests(
            Long id,
            String transactionId,
            EEmailStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.transactionId = transactionId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public EmailRequests(Builder builder) {
        this.id = builder.id;
        this.transactionId = builder.transactionId;
        this.status = builder.status;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public EEmailStatus getStatus() {
        return status;
    }

    public void setStatus(EEmailStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static class Builder {
        private Long id;
        private String transactionId;
        private EEmailStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public Builder status(EEmailStatus status) {
            this.status = status;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public EmailRequests build() {
            return new EmailRequests(this);
        }
    }
}
