package com.hhplus.ticketing.presentation.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class PaymentResponseDto {

    private String status;

    // 작성 에정
    private static PaymentResponseDto from(){
        return PaymentResponseDto.builder().build();
    }

}
