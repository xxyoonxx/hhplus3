package com.hhplus.ticketing.domain.externalAPI.service;

import com.hhplus.ticketing.domain.reservation.event.ReservationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalAPIService {

    public void sendReservationInfo (ReservationEvent reservationEvent){
        log.info("Reservation Info sent");
    }

}
