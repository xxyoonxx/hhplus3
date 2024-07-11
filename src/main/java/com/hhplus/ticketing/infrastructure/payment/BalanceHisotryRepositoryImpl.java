package com.hhplus.ticketing.infrastructure.payment;

import com.hhplus.ticketing.domain.payment.entity.BalanceHistory;
import com.hhplus.ticketing.domain.payment.repository.BalanceHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BalanceHisotryRepositoryImpl implements BalanceHistoryRepository {

    private final BalanceHistoryJpaRepository balanceHistoryJpaRepository;

    @Override
    public BalanceHistory save(BalanceHistory balanceHistory) {
        return balanceHistoryJpaRepository.save(balanceHistory);
    }
}
