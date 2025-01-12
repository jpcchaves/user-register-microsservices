package com.jpcchaves.emailservice.core.repository;

import com.jpcchaves.emailservice.core.model.EmailRequests;

import java.util.Optional;
import jdk.jfr.Registered;

import org.springframework.data.jpa.repository.JpaRepository;

@Registered
public interface EmailRequestsRepository extends JpaRepository<EmailRequests, Long> {
    Boolean existsByIdAndTransactionId(Long id, Long transactionId);

    Optional<EmailRequests> findByIdAndTransactionId(Long id, Long transactionId);
}
