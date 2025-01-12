package com.jpcchaves.emailservice.core.repository;

import com.jpcchaves.emailservice.core.model.EmailRequests;

import jdk.jfr.Registered;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Registered
public interface EmailRequestsRepository extends JpaRepository<EmailRequests, Long> {
    Boolean existsByIdAndTransactionId(Long id, String transactionId);

    Optional<EmailRequests> findByIdAndTransactionId(Long id, String transactionId);
}
