package com.hhplus.ticketing.presentation.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class ReservationResponseDto {

    private long reservationId;
    private String status;

    // 작성 에정
    private static ReservationResponseDto from(){
        return ReservationResponseDto.builder().build();
    }

}
