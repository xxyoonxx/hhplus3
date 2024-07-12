package com.hhplus.ticketing.infrastructure.concert;

import com.hhplus.ticketing.domain.concert.entity.ConcertSeat;
import com.hhplus.ticketing.domain.concert.repository.ConcertSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConcertSeatRepositoryImpl implements ConcertSeatRepository {

    private final ConcertSeatJpaRepository concertSeatJpaRepository;

    @Override
    public List<ConcertSeat> getConcertSeatsInfo(long concertDetailId) {
        return concertSeatJpaRepository.findByConcertDetailDetailId(concertDetailId);
    }

    @Override
    public ConcertSeat getConcertSeatInfo(long seatId) {
        return concertSeatJpaRepository.findById(seatId).get();
    }

    @Override
    public ConcertSeat saveConcertSeat(ConcertSeat concertSeat) {
        return concertSeatJpaRepository.save(concertSeat);
    }

}
