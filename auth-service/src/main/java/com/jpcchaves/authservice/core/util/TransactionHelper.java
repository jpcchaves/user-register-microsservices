package com.jpcchaves.authservice.core.util;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class TransactionHelper {

    public String generateTransactionId() {
        return Instant.now().toEpochMilli() + "-" + UUID.randomUUID();
    }
}
