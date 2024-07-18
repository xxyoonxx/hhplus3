package com.hhplus.ticketing.domain.payment.repository;

import com.hhplus.ticketing.domain.payment.entity.Payment;

public interface PaymentRepository {

    Payment save(Payment payment);
    Payment findByReservationId(long reservationId);

}
