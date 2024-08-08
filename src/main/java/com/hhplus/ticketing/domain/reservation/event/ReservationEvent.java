package com.hhplus.ticketing.domain.reservation.event;

import com.hhplus.ticketing.domain.payment.entity.Payment;
import com.hhplus.ticketing.domain.reservation.entity.Reservation;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ReservationEvent extends ApplicationEvent {

    private Reservation reservation;
    private Payment payment;

    public ReservationEvent(Object source, Reservation reservation, Payment payment) {
        super(source);
        this.reservation = reservation;
        this.payment = payment;
    }
}
