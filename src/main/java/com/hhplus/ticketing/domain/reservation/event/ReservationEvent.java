package com.hhplus.ticketing.domain.reservation.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ReservationEvent extends ApplicationEvent {

    private long reservationId;


    public ReservationEvent(Object source, long reservationId) {
        super(source);
        this.reservationId = reservationId;
    }
}
