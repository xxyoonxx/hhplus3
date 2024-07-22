package com.hhplus.ticketing.presentation.concert.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class ConcertResponseDto {

    private long concertId;
    private String title;

    // 작성 에정
    private static ConcertResponseDto from(){
        return ConcertResponseDto.builder().build();
    }

}
