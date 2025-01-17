package com.jpcchaves.authservice.core.repository;

import com.jpcchaves.authservice.core.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select exists (select 1 from users where email = :email)", nativeQuery = true)
    Boolean existsByEmail(String email);
}
