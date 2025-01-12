package com.jpcchaves.orchestrator.core.enums;

public enum ETopics {
    EMAIL_SUCCESS("email-success"),
    EMAIL_FAIL("email-fail"),
    REGISTRATION_INITIATED("registration-initiated"),
    REGISTRATION_COMPLETED("registration-completed"),
    ORCHESTRATOR("orchestrator");

    private final String topic;

    ETopics(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }
}
