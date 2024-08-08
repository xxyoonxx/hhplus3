package com.hhplus.ticketing.application.payment.service;

import com.hhplus.ticketing.common.exception.CustomException;
import com.hhplus.ticketing.domain.payment.PaymentErrorCode;
import com.hhplus.ticketing.domain.payment.entity.Balance;
import com.hhplus.ticketing.domain.payment.entity.BalanceHistory;
import com.hhplus.ticketing.domain.payment.entity.Payment;
import com.hhplus.ticketing.domain.payment.repository.BalanceHistoryRepository;
import com.hhplus.ticketing.domain.payment.repository.BalanceRepository;
import com.hhplus.ticketing.domain.payment.repository.PaymentRepository;
import com.hhplus.ticketing.domain.reservation.ReservationErrorCode;
import com.hhplus.ticketing.domain.reservation.entity.Reservation;
import com.hhplus.ticketing.domain.reservation.repository.ReservationRepository;
import com.hhplus.ticketing.domain.userQueue.event.UserQueueEvent;
import com.hhplus.ticketing.presentation.payment.dto.BalanceRequestDto;
import com.hhplus.ticketing.presentation.payment.dto.PaymentRequestDto;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final BalanceRepository balanceRepository;
    private final BalanceHistoryRepository balanceHistoryRepository;
    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;

    private final RedissonClient redissonClient;

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 결제 처리
     * @param paymentRequestDto
     * @return
     */
    @Transactional
    public Payment createPayment(PaymentRequestDto paymentRequestDto) {
        // 예약 정보 확인
        Reservation reservation = reservationRepository.getReservationInfo(paymentRequestDto.getReservationId())
                .orElseThrow(() -> new CustomException(ReservationErrorCode.NO_RESERVATION_INFO));

        long userId = reservation.getUserId();

        // 잔액 확인
        Balance balance = userInfoValidation(userId);
        if(balance.getBalance() < reservation.getTotalPrice()) throw new CustomException(PaymentErrorCode.NOT_ENOUGH_MONEY);
        int useAmount = reservation.getTotalPrice();

        // 잔액 차감
        balance.useBalance(useAmount);

        // 충전/사용내역 등록
        BalanceHistory history = new BalanceHistory(balance, useAmount, BalanceHistory.Type.USE);
        balanceHistoryRepository.save(history);

        // 대기열/예약 만료 이벤트 발행
        applicationEventPublisher.publishEvent(new UserQueueEvent(this, userId, Reservation.Status.DONE));

        return paymentRepository.findByReservationId(paymentRequestDto.getReservationId());
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
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Balance chargeBalance(long userId, BalanceRequestDto requestDto) {

        String lockKey = "balanceLock: " + userId;
        RLock lock = redissonClient.getLock(lockKey);
        boolean isLocked = false;

        Balance balance;

        try {
            isLocked = lock.tryLock(5, 10, TimeUnit.SECONDS);
            if (!isLocked) throw new RuntimeException();

            int chargeAmount = requestDto.getAmount();
            if(chargeAmount <= 0) throw new CustomException(PaymentErrorCode.INVALID_CHARGE_AMOUNT);

            balance = userInfoValidation(userId);
            balance.chargeBalance(chargeAmount);

            BalanceHistory history = new BalanceHistory(balance, chargeAmount, BalanceHistory.Type.CHARGE);
            balanceHistoryRepository.save(history);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (isLocked) {
                lock.unlock();
            }
        }

        return balance;
    }

    /**
     * 유저 정보 조회
     * balance테이블에 유저 정보 없을 시 잔액 0으로 insert
     *
     * @param userId
     * @return
     */
    @Transactional
    public Balance userInfoValidation(long userId) {
        Balance balance = balanceRepository.getBalance(userId);
        if(balance==null) balance = balanceRepository.save(new Balance(userId, 0));
        return balance;
    }

}
