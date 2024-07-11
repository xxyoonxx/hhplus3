package com.hhplus.ticketing.domain.concert.repository;

import com.hhplus.ticketing.domain.concert.entity.ConcertSeat;

import java.util.List;

public interface ConcertSeatRepository {

    List<ConcertSeat> getConcertSeatsInfo(long concertDetailId);

}
