package com.hhplus.ticketing.domain.reservation.event;

import com.hhplus.ticketing.domain.externalAPI.service.ExternalAPIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationEventListener {

    private ExternalAPIService externalAPIService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void dataSendHandler(ReservationEvent reservationEvent) {
        try {
            externalAPIService.sendReservationInfo(reservationEvent);
        } catch(Exception e) {
            log.error("dataSendHandler failed");
        }
    }

}
