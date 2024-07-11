package com.hhplus.ticketing.presentation.concert.dto;

import com.hhplus.ticketing.domain.concert.entity.ConcertSeat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
@Data
public class ConcertSeatResponseDto {

    private long seatId;

    public static List<ConcertSeatResponseDto> from(List<ConcertSeat> concertSeats) {
        return concertSeats.stream()
                .map(ConcertSeatResponseDto::from)
                .collect(Collectors.toList());
    }

    public static ConcertSeatResponseDto from(ConcertSeat concertSeat) {
        return ConcertSeatResponseDto.builder()
                .seatId(concertSeat.getSeatId())
                .build();
    }

}
