package com.jpcchaves.authservice.core.exception.dto;

import java.time.LocalDateTime;

public class ExceptionResponse {
    private String message;
    private String details;
    private Integer status;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ExceptionResponse(Builder builder) {
        this.message = builder.message;
        this.details = builder.details;
        this.status = builder.status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String message;
        private String details;
        private Integer status;

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withDetails(String details) {
            this.details = details;
            return this;
        }

        public Builder withStatus(Integer status) {
            this.status = status;
            return this;
        }

        public ExceptionResponse build() {
            return new ExceptionResponse(this);
        }
    }
}
