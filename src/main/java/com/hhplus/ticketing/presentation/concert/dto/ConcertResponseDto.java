package com.hhplus.ticketing.presentation.concert.dto;

import com.hhplus.ticketing.domain.concert.entity.Concert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ConcertResponseDto {

    private long concertId;
    private String title;

    public static ConcertResponseDto from(Concert concert) {
        return ConcertResponseDto.builder()
                .concertId(concert.getConcertId())
                .title(concert.getConcertTitle())
                .build();
    }

}
