package com.dj.ckw.userservice.repository;

import com.dj.ckw.userservice.model.EmailVerifications;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationsRepository extends CrudRepository<EmailVerifications, UUID> {
    Optional<EmailVerifications> findByEmail(String email);
}
