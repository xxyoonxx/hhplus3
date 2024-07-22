package com.hhplus.ticketing.infrastructure.reservation;

import com.hhplus.ticketing.domain.reservation.entity.Reservation;
import com.hhplus.ticketing.domain.userQueue.entity.UserQueue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationJpaRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByReservationDateBeforeAndStatusNot(LocalDateTime expiredReservationTime, Reservation.Status status);

    Optional<Reservation> findByUserId(Long userId);
}
