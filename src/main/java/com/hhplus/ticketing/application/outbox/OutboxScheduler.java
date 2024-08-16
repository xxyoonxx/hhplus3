package com.hhplus.ticketing.application.outbox;

import com.hhplus.ticketing.common.kafka.KafkaProducer;
import com.hhplus.ticketing.domain.outbox.entity.Outbox;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxScheduler {

    private final OutboxService outboxService;
    private final KafkaProducer kafkaProducer;

    @Scheduled(fixedDelay = 5 * 60 * 1000)
    public void publishMessages() {
        List<Outbox> initList = outboxService.findByType(Outbox.Type.INIT);
        initList.forEach(outbox -> {
            String topic = switch (outbox.getDomain()) {
                case RESERVATION -> "reservationTopic";
                case USERQUEUE -> "userQueueTopic";
            };
            kafkaProducer.publish(topic, outbox.getMessage());
        });
    }

}