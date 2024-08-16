package com.hhplus.ticketing.infrastructure.outbox;

import com.hhplus.ticketing.domain.outbox.entity.Outbox;
import com.hhplus.ticketing.domain.outbox.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OutboxRepositoryImpl implements OutboxRepository {

    private final OutboxJpaRepository outboxJpaRepository;

    @Override
    public Outbox save(Outbox outbox) {
        return outboxJpaRepository.save(outbox);
    }

    @Override
    public List<Outbox> findByType(Outbox.Type type) {
        return outboxJpaRepository.findByType(type);
    }

    @Override
    public Outbox findByMessageAndDomain(String message, Outbox.Domain domain) {
        return outboxJpaRepository.findByMessageAndDomain(message, domain);
    }

}
