package com.hhplus.ticketing.infrastructure.reservation;

import com.hhplus.ticketing.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationJpaRepository extends JpaRepository<Reservation, Long> {
}
