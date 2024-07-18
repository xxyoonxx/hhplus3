package com.hhplus.ticketing.infrastructure.payment;

import com.hhplus.ticketing.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {
    Payment findByReservationReservationId(long reservationId);

}
