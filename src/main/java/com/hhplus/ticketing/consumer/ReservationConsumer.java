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
public class ReservationConsumer {

    private final OutboxService outboxService;

    @KafkaListener(topics = "reservationTopic", groupId = "reservationGroup")
    public void listen(ConsumerRecord<String, String> record) {
        String reservationId = record.value();
        log.info("reservationId: {}", reservationId);
        Outbox outbox = outboxService.findByMessageAndDomain(reservationId, Outbox.Domain.RESERVATION);
        outbox.changeTypeToPublished();
        outboxService.save(outbox);
    }

}
