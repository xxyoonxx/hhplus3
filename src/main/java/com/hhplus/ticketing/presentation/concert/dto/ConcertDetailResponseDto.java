package com.hhplus.ticketing.presentation.concert.dto;

import com.hhplus.ticketing.domain.concert.entity.ConcertDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
@Data
public class ConcertDetailResponseDto {

    private long detailId;
    private LocalDateTime concertDate;

    public static ConcertDetailResponseDto from(ConcertDetail detail) {
        return ConcertDetailResponseDto.builder()
                .detailId(detail.getDetailId())
                .concertDate(detail.getConcertDate())
                .build();
    }

    public static List<ConcertDetailResponseDto> from(List<ConcertDetail> details) {
        return details.stream()
                .map(ConcertDetailResponseDto::from)
                .collect(Collectors.toList());
    }

}
