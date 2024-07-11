package com.hhplus.ticketing.domain.payment.repository;

import com.hhplus.ticketing.domain.payment.entity.BalanceHistory;

public interface BalanceHistoryRepository {

    BalanceHistory save(BalanceHistory balanceHistory);

}
