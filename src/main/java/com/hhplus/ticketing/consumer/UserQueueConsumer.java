package com.hhplus.ticketing.consumer;

import com.hhplus.ticketing.application.outbox.OutboxService;
import com.hhplus.ticketing.domain.outbox.entity.Outbox;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserQueueConsumer {

    private final OutboxService outboxService;

    @KafkaListener(topics = "userQueueTopic", groupId = "userQueueGroup")
    public void listen(ConsumerRecord<String, String> record) {
        String userId = record.value();
        log.info("userId: {}", userId);
        Outbox outbox = outboxService.findByMessageAndDomain(userId, Outbox.Domain.USERQUEUE);
        outbox.changeTypeToPublished();
        outboxService.save(outbox);
    }

}
