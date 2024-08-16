package com.hhplus.ticketing.domain.userQueue.event;

import com.hhplus.ticketing.application.outbox.OutboxService;
import com.hhplus.ticketing.application.userQueue.service.UserQueueProcessService;
import com.hhplus.ticketing.common.kafka.KafkaProducer;
import com.hhplus.ticketing.domain.outbox.entity.Outbox;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserQueueEventListener {

    private final KafkaProducer kafkaProducer;
    private final OutboxService outboxService;
    private final UserQueueProcessService userQueueProcessService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void outboxDataHandler(UserQueueEvent userQueueEvent) {
        Outbox outbox = Outbox.of(Outbox.Type.INIT, Outbox.Domain.USERQUEUE, String.valueOf(userQueueEvent.getUserId()));
        outboxService.save(outbox);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void QueueExpireHandler(UserQueueEvent userQueueEvent){
        kafkaProducer.publish("userQueueTopic", String.valueOf(userQueueEvent.getUserId()));
        userQueueProcessService.expireQueue(userQueueEvent.getUserId(), userQueueEvent.getStatus());
    }

}
