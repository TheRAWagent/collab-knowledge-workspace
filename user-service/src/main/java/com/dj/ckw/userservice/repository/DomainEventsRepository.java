package com.dj.ckw.userservice.repository;

import com.dj.ckw.userservice.model.DomainEvents;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface DomainEventsRepository extends CrudRepository<DomainEvents, UUID> {
    Optional<DomainEvents> findFirstByEventTypeAndEventStatus(String eventType, String eventStatus);
}
