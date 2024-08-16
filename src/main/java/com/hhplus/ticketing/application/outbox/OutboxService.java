package com.hhplus.ticketing.application.outbox;

import com.hhplus.ticketing.domain.outbox.entity.Outbox;
import com.hhplus.ticketing.domain.outbox.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxRepository outboxRepository;

    public Outbox save(Outbox outbox) {
        return outboxRepository.save(outbox);
    }

    public List<Outbox> findByType(Outbox.Type type) {
        return outboxRepository.findByType(type);
    }

    public Outbox findByMessageAndDomain(String message, Outbox.Domain domain) {
        return outboxRepository.findByMessageAndDomain(message, domain);
    }

}
