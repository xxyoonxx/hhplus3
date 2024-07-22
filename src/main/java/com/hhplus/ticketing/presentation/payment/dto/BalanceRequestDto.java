package com.hhplus.ticketing.presentation.payment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BalanceRequestDto {

    private int amount;

}
