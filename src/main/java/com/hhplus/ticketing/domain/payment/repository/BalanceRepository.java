package com.hhplus.ticketing.domain.payment.repository;

import com.hhplus.ticketing.domain.payment.entity.Balance;

public interface BalanceRepository {
    Balance getBalance(Long userId);

    Balance getBalanceWithLock(Long userId);

    Balance save(Balance balance);
}
