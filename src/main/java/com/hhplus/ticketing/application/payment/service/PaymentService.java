package com.hhplus.ticketing.application.payment.service;

import com.hhplus.ticketing.common.exception.CustomException;
import com.hhplus.ticketing.domain.payment.PaymentErrorCode;
import com.hhplus.ticketing.domain.payment.entity.Balance;
import com.hhplus.ticketing.domain.payment.entity.BalanceHistory;
import com.hhplus.ticketing.domain.payment.entity.Payment;
import com.hhplus.ticketing.domain.payment.repository.BalanceHistoryRepository;
import com.hhplus.ticketing.domain.payment.repository.BalanceRepository;
import com.hhplus.ticketing.domain.payment.repository.PaymentRepository;
import com.hhplus.ticketing.domain.queue.QueueErrorCode;
import com.hhplus.ticketing.domain.queue.entity.Queue;
import com.hhplus.ticketing.domain.queue.repository.QueueRepository;
import com.hhplus.ticketing.domain.reservation.ReservationErrorCode;
import com.hhplus.ticketing.domain.reservation.entity.Reservation;
import com.hhplus.ticketing.domain.reservation.repository.ReservationRepository;
import com.hhplus.ticketing.presentation.payment.dto.BalanceRequestDto;
import com.hhplus.ticketing.presentation.payment.dto.PaymentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final BalanceRepository balanceRepository;
    private final BalanceHistoryRepository balanceHistoryRepository;
    private final PaymentRepository paymentRepository;

    private final QueueRepository queueRepository;
    private final ReservationRepository reservationRepository;

    /**
     * 결제 처리
     * @param authorization
     * @param paymentRequestDto
     * @return
     */
    public Payment createPayment(String authorization, PaymentRequestDto paymentRequestDto) {
        // 토큰 검증
        Queue queue = queueRepository.getUserIdByToken(authorization).orElseThrow(() -> new CustomException(QueueErrorCode.USER_NOT_FOUND));
        if (queue.getStatus() == Queue.Status.EXPIRED) throw new CustomException(QueueErrorCode.TOKEN_EXPIRED);
        long userId = queue.getUserId();

        // 예약 정보 확인
        Reservation reservation = reservationRepository.getReservationInfo(paymentRequestDto.getReservationId())
                .orElseThrow(() -> new CustomException(ReservationErrorCode.NO_RESERVATION_INFO));

        // 잔액 확인
        Balance balance = userInfoValidation(userId);
        if(balance.getBalance() < reservation.getTotalPrice()) throw new CustomException(PaymentErrorCode.NOT_ENOUGH_MONEY);
        int useAmount = reservation.getTotalPrice();

        // 잔액 차감
        balance = balance.useBalance(useAmount);
        balanceRepository.save(balance);

        // 충전/사용내역 등록
        BalanceHistory history = new BalanceHistory(balance, useAmount, BalanceHistory.Type.USE);
        balanceHistoryRepository.save(history);

        // 결제 이력 등록
        Payment payment = Payment.builder()
                .payAmount(useAmount)
                .status(Payment.Status.DONE)
                .payDate(LocalDateTime.now())
                .reservation(reservation)
                .build();

        return paymentRepository.save(payment);
    }

    /**
     * 잔액 조회
     * @param userId
     * @return
     */
    public Balance getBalance(long userId) {
        Balance balance = userInfoValidation(userId);
        return balance;
    }

    /**
     * 잔액 충전
     * @param userId
     * @param requestDto
     * @return
     */
    public Balance chargeBalance(long userId, BalanceRequestDto requestDto) {
        int chargeAmount = requestDto.getAmount();
        if(chargeAmount <= 0) throw new CustomException(PaymentErrorCode.INVALID_CHARGE_AMOUNT);
        Balance balance = userInfoValidation(userId);
        balance = balance.chargeBalance(chargeAmount);

        BalanceHistory history = new BalanceHistory(balance, chargeAmount, BalanceHistory.Type.CHARGE);
        balanceHistoryRepository.save(history);

        return balanceRepository.save(balance);
    }

    /**
     * 유저 정보 조회
     * balance테이블에 유저 정보 없을 시 잔액 0으로 insert
     *
     * @param userId
     * @return
     */
    private Balance userInfoValidation(long userId) {
        Balance balance = balanceRepository.getBalance(userId);
        if(balance==null) balance = balanceRepository.save(new Balance(userId, 0));
        return balance;
    }

}
