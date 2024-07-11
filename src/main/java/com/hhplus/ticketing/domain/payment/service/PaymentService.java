package com.hhplus.ticketing.domain.payment.service;

import com.hhplus.ticketing.common.exception.CustomException;
import com.hhplus.ticketing.domain.payment.PaymentErrorCode;
import com.hhplus.ticketing.domain.payment.entity.Balance;
import com.hhplus.ticketing.domain.payment.entity.BalanceHistory;
import com.hhplus.ticketing.domain.payment.repository.BalanceHistoryRepository;
import com.hhplus.ticketing.domain.payment.repository.BalanceRepository;
import com.hhplus.ticketing.presentation.payment.dto.BalanceRequestDto;
import com.hhplus.ticketing.presentation.payment.dto.BalanceResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final BalanceRepository balanceRepository;
    private final BalanceHistoryRepository balanceHistoryRepository;

    /**
     * 잔액 조회
     *
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
        balanceRepository.save(balance);

        BalanceHistory history = new BalanceHistory(balance, chargeAmount, BalanceHistory.Type.CHARGE);
        balanceHistoryRepository.save(history);

        return balance;
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
