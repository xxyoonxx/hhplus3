package com.hhplus.ticketing.presentation.concert.dto;

import com.hhplus.ticketing.domain.concert.entity.Concert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
@Data
public class ConcertResponseDto {

    private long concertId;
    private String title;

    public static List<ConcertResponseDto> from(List<Concert> concerts){
        return concerts.stream()
                .map(concert -> ConcertResponseDto.builder()
                        .concertId(concert.getConcertId())
                        .title(concert.getTitle())
                        .build())
                .collect(Collectors.toList());
    }

}
