package com.hhplus.ticketing.presentation.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReservationRequestDto {

    private long reservationId;
    private long detailId;
    private long seatId;
    private long userId;
    private int totalPrice;
    private LocalDateTime reservationDate;

}
