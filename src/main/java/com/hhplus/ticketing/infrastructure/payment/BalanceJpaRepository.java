package com.hhplus.ticketing.infrastructure.payment;

import com.hhplus.ticketing.domain.payment.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceJpaRepository extends JpaRepository<Balance, Long> {

    Balance getByUserId(long userId);

}
