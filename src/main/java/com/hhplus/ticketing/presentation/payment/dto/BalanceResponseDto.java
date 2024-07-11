package com.hhplus.ticketing.presentation.payment.dto;

import com.hhplus.ticketing.domain.payment.entity.Balance;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BalanceResponseDto {

    public long balanceId;
    public int balance;

    public static BalanceResponseDto from(Balance balance) {
        return BalanceResponseDto.builder()
                .balanceId(balance.getBalanceId())
                .balance(balance.getBalance())
                .build();
    }

}
