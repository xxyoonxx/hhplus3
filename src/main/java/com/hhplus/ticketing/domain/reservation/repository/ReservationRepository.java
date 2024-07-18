package com.hhplus.ticketing.domain.reservation.repository;

import com.hhplus.ticketing.domain.reservation.entity.Reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    Reservation save(Reservation reservation);
    Optional<Reservation> getReservationInfo(Long reservationId);
    List<Reservation> getExpiredReservations(LocalDateTime expiredReservationTime);
    Optional<Reservation> getReservationInfoByUserId(Long userId);

}
