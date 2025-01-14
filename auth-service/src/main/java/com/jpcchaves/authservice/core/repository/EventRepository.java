package com.jpcchaves.authservice.core.repository;

import com.jpcchaves.authservice.core.model.Event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {}
