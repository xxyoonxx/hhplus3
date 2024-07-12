package com.hhplus.ticketing.presentation.payment.dto;

import com.hhplus.ticketing.domain.payment.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class PaymentResponseDto {

    private Payment.Status status;

    public static PaymentResponseDto from(Payment payment){
        return PaymentResponseDto.builder()
                .status(payment.getStatus())
                .build();
    }

}
