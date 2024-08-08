package com.hhplus.ticketing.domain.userQueue.event;

import com.hhplus.ticketing.domain.reservation.entity.Reservation;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class UserQueueEvent extends ApplicationEvent {

    private long userId;
    private Reservation.Status status;

    public UserQueueEvent(Object source, long userId, Reservation.Status status) {
        super(source);
        this.userId = userId;
        this.status = status;
    }

}
