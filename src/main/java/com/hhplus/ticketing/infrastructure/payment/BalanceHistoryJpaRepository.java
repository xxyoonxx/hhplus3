package com.hhplus.ticketing.infrastructure.payment;

import com.hhplus.ticketing.domain.payment.entity.BalanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceHistoryJpaRepository extends JpaRepository<BalanceHistory, Long> {
}
