package com.hhplus.ticketing.presentation.concert.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class ConcertSeatResponseDto {

    private long seatId;

    // 작성 에정
    private static ConcertSeatResponseDto from(){
        return ConcertSeatResponseDto.builder().build();
    }

}
