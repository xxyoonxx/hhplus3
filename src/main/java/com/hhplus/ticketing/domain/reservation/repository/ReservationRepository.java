package com.hhplus.ticketing.domain.reservation.repository;

import com.hhplus.ticketing.domain.reservation.entity.Reservation;

import java.util.Optional;

public interface ReservationRepository {

    Reservation reserve(Reservation reservation);
    Optional<Reservation> getReservationInfo(Long reservationId);

}
