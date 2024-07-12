package com.hhplus.ticketing.presentation.reservation.dto;

import com.hhplus.ticketing.domain.reservation.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class ReservationResponseDto {

    private long reservationId;
    private String status;

    public static ReservationResponseDto from(Reservation reservation) {
        return ReservationResponseDto.builder()
                .reservationId(reservation.getReservationId())
                .status(reservation.getStatus().name())
                .build();
    }

}
