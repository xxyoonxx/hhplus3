package com.hhplus.ticketing.domain;

import com.hhplus.ticketing.domain.payment.entity.Balance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BalanceDomainTest {

    private Balance balance;

    @BeforeEach
    void setUp() {
        // given
        balance = Balance.builder()
                .userId(1L)
                .balance(1000)
                .build();
    }

    @Test
    @DisplayName("잔액 충전")
    void chargeBalance() {
        // when
        balance.chargeBalance(9000);

        // then
        assertEquals(10000,balance.getBalance());
    }

    @Test
    @DisplayName("잔액 사용")
    void useBalance() {
        // when
        balance.useBalance(500);

        // then
        assertEquals(500, balance.getBalance());
    }

}
