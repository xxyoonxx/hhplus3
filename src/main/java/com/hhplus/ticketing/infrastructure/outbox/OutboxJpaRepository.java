package com.hhplus.ticketing.infrastructure.outbox;

import com.hhplus.ticketing.domain.outbox.entity.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxJpaRepository extends JpaRepository<Outbox, Long>  {

    List<Outbox> findByType(Outbox.Type type);
    Outbox findByMessageAndDomain(String message, Outbox.Domain domain);

}
