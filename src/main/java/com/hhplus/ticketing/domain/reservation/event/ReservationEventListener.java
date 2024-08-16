package com.hhplus.ticketing.domain.reservation.event;

import com.hhplus.ticketing.application.outbox.OutboxService;
import com.hhplus.ticketing.common.kafka.KafkaProducer;
import com.hhplus.ticketing.domain.externalAPI.service.ExternalAPIService;
import com.hhplus.ticketing.domain.outbox.entity.Outbox;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationEventListener {

    private final KafkaProducer kafkaProducer;
    private final OutboxService outboxService;
    private final ExternalAPIService externalAPIService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void outboxDataHandler(ReservationEvent reservationEvent){
        Outbox outbox = Outbox.of(Outbox.Type.INIT, Outbox.Domain.RESERVATION,String.valueOf(reservationEvent.getReservationId()));
        outboxService.save(outbox);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void dataSendHandler(ReservationEvent reservationEvent) {
        try {
            kafkaProducer.publish("reservationTopic", String.valueOf(reservationEvent.getReservationId()));
            externalAPIService.sendReservationInfo(reservationEvent);
        } catch(Exception e) {
            log.error("dataSendHandler failed");
        }
    }

}
