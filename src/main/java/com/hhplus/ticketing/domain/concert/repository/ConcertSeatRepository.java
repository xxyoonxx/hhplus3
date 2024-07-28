package com.hhplus.ticketing.domain.concert.repository;

import com.hhplus.ticketing.domain.concert.entity.ConcertSeat;

import java.util.List;
import java.util.Optional;

public interface ConcertSeatRepository {

    List<ConcertSeat> getConcertSeatsInfo(long concertDetailId);
    Optional<ConcertSeat> getConcertSeatInfo(long seatId);
    ConcertSeat save(ConcertSeat concertSeat);

}
