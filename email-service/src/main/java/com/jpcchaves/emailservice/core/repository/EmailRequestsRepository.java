package com.jpcchaves.emailservice.core.repository;

import com.jpcchaves.emailservice.core.model.EmailRequests;

import jdk.jfr.Registered;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Registered
public interface EmailRequestsRepository extends JpaRepository<EmailRequests, Long> {
    Boolean existsByTransactionId(String transactionId);

    Optional<EmailRequests> findByTransactionId(String transactionId);
}
