package com.jpcchaves.authservice.core.service;

import com.jpcchaves.authservice.core.model.Event;
import com.jpcchaves.authservice.core.producer.SagaProducer;
import com.jpcchaves.authservice.core.util.JsonUtil;
import com.jpcchaves.authservice.core.util.TransactionHelper;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final JsonUtil jsonUtil;
    private final SagaProducer sagaProducer;
    private final TransactionHelper transactionHelper;

    public AuthService(
            JsonUtil jsonUtil, SagaProducer sagaProducer, TransactionHelper transactionHelper) {
        this.jsonUtil = jsonUtil;
        this.sagaProducer = sagaProducer;
        this.transactionHelper = transactionHelper;
    }

    public void register() {

        sagaProducer.sendEvent(jsonUtil.toJson(createPayload()));
    }

    private Event createPayload() {
        return Event.builder()
                .transactionId(transactionHelper.generateTransactionId())
                .payload("Teste")
                .createdAt(LocalDateTime.now())
                .build();
    }
}
