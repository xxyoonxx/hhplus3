package com.hhplus.ticketing.service;

import com.hhplus.ticketing.application.userQueue.service.UserQueueProcessService;
import com.hhplus.ticketing.domain.concert.entity.ConcertSeat;
import com.hhplus.ticketing.domain.payment.entity.Payment;
import com.hhplus.ticketing.application.payment.service.PaymentService;
import com.hhplus.ticketing.domain.payment.entity.Balance;
import com.hhplus.ticketing.domain.payment.entity.BalanceHistory;
import com.hhplus.ticketing.domain.payment.repository.BalanceHistoryRepository;
import com.hhplus.ticketing.domain.payment.repository.BalanceRepository;
import com.hhplus.ticketing.domain.payment.repository.PaymentRepository;
import com.hhplus.ticketing.domain.userQueue.entity.UserQueue;
import com.hhplus.ticketing.domain.userQueue.repository.UserQueueRepository;
import com.hhplus.ticketing.domain.reservation.entity.Reservation;
import com.hhplus.ticketing.domain.reservation.repository.ReservationRepository;
import com.hhplus.ticketing.presentation.payment.dto.BalanceRequestDto;
import com.hhplus.ticketing.presentation.payment.dto.PaymentRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private BalanceRepository balanceRepository;

    @Mock
    private BalanceHistoryRepository balanceHistoryRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private UserQueueRepository userQueueRepository;

    @Mock
    private UserQueueProcessService userQueueProcessService;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    @DisplayName("결제 처리")
    void payCharge(){
        ConcertSeat seat01 = ConcertSeat.builder()
                .seatId(1L)
                .seatNo("A01")
                .seatPrice(10000)
                .status(ConcertSeat.Status.AVAILABLE)
                .build();

        Reservation reservation = Reservation.builder()
                .reservationId(1L)
                .userId(1L)
                .concertSeat(seat01)
                .concertTitle("Concert")
                .reservationDate(LocalDateTime.now())
                .status(Reservation.Status.WAITING)
                .totalPrice(10000)
                .build();

        Balance balance = Balance.builder()
                .userId(1L)
                .balance(20000)
                .build();

        UserQueue userQueue = UserQueue.builder()
                .userId(1L)
                .status(UserQueue.Status.PROCESSING)
                .build();

        PaymentRequestDto paymentRequestDto = PaymentRequestDto.builder()
                .reservationId(1L)
                .build();

        when(userQueueRepository.getTokenInfo("test-token")).thenReturn(Optional.ofNullable(userQueue));
        when(reservationRepository.getReservationInfo(1L)).thenReturn(Optional.of(reservation));
        when(balanceRepository.getBalance(1L)).thenReturn(balance);

        Payment mockPayment = Payment.builder()
                .reservation(reservation)
                .status(Payment.Status.DONE)
                .build();

        when(paymentRepository.findByReservationId(1L)).thenReturn(mockPayment);

        Payment createdPayment = paymentService.createPayment("test-token", paymentRequestDto);

        assertNotNull(createdPayment);
        assertEquals(reservation.getReservationId(), createdPayment.getReservation().getReservationId());
        assertEquals(Payment.Status.DONE, createdPayment.getStatus());
    }

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