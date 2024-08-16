package com.hhplus.ticketing.outbox;

import com.hhplus.ticketing.application.outbox.OutboxScheduler;
import com.hhplus.ticketing.domain.outbox.entity.Outbox;
import com.hhplus.ticketing.domain.outbox.repository.OutboxRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OutboxSchedulerIntegrationTest {

    @LocalServerPort
    protected int port;

    @Autowired
    private OutboxRepository outboxRepository;

    @Autowired
    private OutboxScheduler outboxScheduler;

    @Test
    @DisplayName("스케줄러 로직 테스트")
    void outboxSchedulerTest() {
        // when
        outboxScheduler.publishMessages();

        // then
        List<Outbox> remainingOutbox = outboxRepository.findByType(Outbox.Type.INIT);
        assertTrue(remainingOutbox.isEmpty());
    }

}
