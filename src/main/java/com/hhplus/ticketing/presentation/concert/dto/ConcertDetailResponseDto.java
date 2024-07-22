package com.hhplus.ticketing.presentation.concert.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Data
public class ConcertDetailResponseDto {

    private long detailId;
    private LocalDateTime concertDate;

    // 작성 에정
    private static ConcertDetailResponseDto from(){
        return ConcertDetailResponseDto.builder().build();
    }

}
