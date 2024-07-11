package com.hhplus.ticketing.service;

import com.hhplus.ticketing.domain.payment.service.PaymentService;
import com.hhplus.ticketing.domain.payment.entity.Balance;
import com.hhplus.ticketing.domain.payment.entity.BalanceHistory;
import com.hhplus.ticketing.domain.payment.repository.BalanceHistoryRepository;
import com.hhplus.ticketing.domain.payment.repository.BalanceRepository;
import com.hhplus.ticketing.presentation.payment.dto.BalanceRequestDto;
import com.hhplus.ticketing.presentation.payment.dto.BalanceResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private BalanceRepository balanceRepository;

    @Mock
    private BalanceHistoryRepository balanceHistoryRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    @DisplayName("잔액 조회 - Balance테이블에 userId 존재")
    void getExistBalanceInfo() {
        Long userId = 1L;
        int amount = 100;
        Balance balance = Balance.builder()
                .userId(userId)
                .balance(amount)
                .build();

        when(balanceRepository.getBalance(userId)).thenReturn(balance);
        Balance balanceInfo = paymentService.getBalance(userId);

        assertThat(balanceInfo.getBalance()).isEqualTo(100);
    }

    @Test
    @DisplayName("잔액 조회 - Balance테이블에 userId 부재")
    void getNotExistBalanceInfo() {
        long userId = 1L;
        Balance balance = Balance.builder()
                .userId(userId)
                .build();

        when(balanceRepository.getBalance(userId)).thenReturn(null).thenReturn(balance);
        when(balanceRepository.save(any(Balance.class))).thenReturn(balance);
        Balance balanceInfo = paymentService.getBalance(userId);

        assertThat(balanceInfo.getBalance()).isEqualTo(0);
    }

    @Test
    @DisplayName("잔액 충전")
    void chargeBalance() {
        long userId = 1L;
        int currentAmount = 100;
        Balance balance = Balance.builder()
                .userId(userId)
                .balance(currentAmount)
                .build();

        int chargeAmount = 1000;
        BalanceRequestDto requestDto = BalanceRequestDto.builder()
                .amount(chargeAmount)
                .build();
        BalanceHistory balanceHistory = BalanceHistory.builder()
                .balance(balance)
                .amount(chargeAmount)
                .type(BalanceHistory.Type.CHARGE)
                .build();

        when(balanceRepository.getBalance(userId)).thenReturn(balance);
        when(balanceRepository.save(any(Balance.class))).thenReturn(balance);
        when(balanceHistoryRepository.save(any(BalanceHistory.class))).thenReturn(balanceHistory);

        Balance chargeBalance = paymentService.chargeBalance(userId, requestDto);

        assertThat(chargeBalance.getBalance()).isEqualTo(1100);
    }
}