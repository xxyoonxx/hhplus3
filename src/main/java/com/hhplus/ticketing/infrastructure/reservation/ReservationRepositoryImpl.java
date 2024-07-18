package com.hhplus.ticketing.infrastructure.reservation;

import com.hhplus.ticketing.domain.reservation.entity.Reservation;
import com.hhplus.ticketing.domain.reservation.repository.ReservationRepository;
import com.hhplus.ticketing.domain.userQueue.entity.UserQueue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepository {

    private final ReservationJpaRepository reservationJpaRepository;

    @Override
    public Reservation save(Reservation reservation) {
        return reservationJpaRepository.save(reservation);
    }

    @Override
    public Optional<Reservation> getReservationInfo(Long reservationId) {
        return reservationJpaRepository.findById(reservationId);
    }

    @Override
    public List<Reservation> getExpiredReservations(LocalDateTime expiredReservationTime) {
        return reservationJpaRepository.findByReservationDateBeforeAndStatusNot(expiredReservationTime, Reservation.Status.EXPIRED);
    }

    @Override
    public Optional<Reservation> getReservationInfoByUserId(Long userId) {
        return reservationJpaRepository.findByUserId(userId);
    }

}
