package com.hhplus.ticketing.presentation.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class PaymentRequestDto {

    private long reservationId;

}
