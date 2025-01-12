package com.jpcchaves.emailservice.core.dto;

import com.jpcchaves.emailservice.core.enums.ESagaStatus;
import java.time.LocalDateTime;

public class HistoryDTO {
    private String source;
    private ESagaStatus status;
    private String message;
    private LocalDateTime createdAt;

    public HistoryDTO() {}

    public HistoryDTO(String source, ESagaStatus status, String message, LocalDateTime createdAt) {
        this.source = source;
        this.status = status;
        this.message = message;
        this.createdAt = createdAt;
    }

    public HistoryDTO(Builder builder) {
        this.source = builder.source;
        this.status = builder.status;
        this.message = builder.message;
        this.createdAt = builder.createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public ESagaStatus getStatus() {
        return status;
    }

    public void setStatus(ESagaStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static class Builder {
        private String source;
        private ESagaStatus status;
        private String message;
        private LocalDateTime createdAt;

        public Builder() {}

        public Builder source(String source) {
            this.source = source;
            return this;
        }

        public Builder status(ESagaStatus status) {
            this.status = status;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public HistoryDTO build() {
            return new HistoryDTO(this);
        }
    }
}
