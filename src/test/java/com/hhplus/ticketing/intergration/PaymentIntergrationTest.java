package com.hhplus.ticketing.intergration;

import com.hhplus.ticketing.application.payment.service.PaymentService;
import com.hhplus.ticketing.common.exception.CustomException;
import com.hhplus.ticketing.domain.payment.entity.Balance;
import com.hhplus.ticketing.domain.payment.entity.Payment;
import com.hhplus.ticketing.domain.payment.repository.BalanceRepository;
import com.hhplus.ticketing.domain.payment.repository.PaymentRepository;
import com.hhplus.ticketing.domain.reservation.repository.ReservationRepository;
import com.hhplus.ticketing.presentation.payment.dto.BalanceRequestDto;
import com.hhplus.ticketing.presentation.payment.dto.PaymentRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
//@Import(TestSetupConfig.class)
public class PaymentIntergrationTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private ReservationRepository reservationRepository;
/*

    @Autowired
    private ConcertSeat seat01;

    @Autowired
    private ConcertSeat seat02;

    @Autowired
    private ConcertDetail concertDetail;

    @Autowired
    private Concert concert;

    @Autowired
    private Reservation reservation;
*/
    @Test
    @DisplayName("동시성 테스트 - 충전")
    void chargeConcurrencyTest() throws InterruptedException {
        int amount = 1000;
        BalanceRequestDto balanceRequestDto = BalanceRequestDto.builder()
                .amount(amount)
                .build();

        final int threadCount = 10;
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger fail = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    paymentService.chargeBalance(1, balanceRequestDto);
                    System.err.println("successCount: " + success.incrementAndGet());
                } catch(Exception e) {
                    System.err.println("failCount: " + fail.incrementAndGet());
                    System.err.println(e.getMessage());
                } finally {
                    countDownLatch.countDown();
                }
            }).start();
        }
        countDownLatch.await();
        Thread.sleep(1000);

        assertEquals(amount * threadCount, paymentService.getBalance(1));
        assertEquals(success.get(),threadCount);

    }

    @Test
    @DisplayName("결제 처리")
    void payCharge() {

        Balance balance = Balance.builder()
                .userId(1L)
                .balance(20000)
                .build();
        balanceRepository.save(balance);

        PaymentRequestDto paymentRequestDto = PaymentRequestDto.builder()
                .reservationId(1L)
                .build();

        Payment createdPayment = paymentService.createPayment(paymentRequestDto);

        assertNotNull(createdPayment);
        assertEquals(1, createdPayment.getReservation().getReservationId());
        assertEquals(Payment.Status.DONE, createdPayment.getStatus());

    }

    @Test
    @DisplayName("결제 불가")
    void payChargeException() {

        // 예약 정보 없음
        PaymentRequestDto noUser = PaymentRequestDto.builder()
                .reservationId(0L)
                .build();

        CustomException ex = assertThrows(CustomException.class, ()-> paymentService.createPayment(noUser));

        assertEquals("예약 정보를 찾을 수 없습니다.", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getErrorCode());

        // 잔액 부족
        Balance balance = Balance.builder()
                .userId(1L)
                .balance(100)
                .build();
        balanceRepository.save(balance);

        PaymentRequestDto paymentRequestDto = PaymentRequestDto.builder()
                .reservationId(1L)
                .build();

        ex = assertThrows(CustomException.class, ()-> paymentService.createPayment(paymentRequestDto));

        assertEquals("잔액이 부족합니다.", ex.getMessage());
        assertEquals(HttpStatus.PAYMENT_REQUIRED, ex.getErrorCode());


    }

}
