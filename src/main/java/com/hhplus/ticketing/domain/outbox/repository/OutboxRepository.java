package com.hhplus.ticketing.domain.outbox.repository;

import com.hhplus.ticketing.domain.outbox.entity.Outbox;

import java.util.List;

public interface OutboxRepository {

    Outbox save(Outbox outbox);
    List<Outbox> findByType(Outbox.Type type);
    Outbox findByMessageAndDomain(String message, Outbox.Domain domain);

}
