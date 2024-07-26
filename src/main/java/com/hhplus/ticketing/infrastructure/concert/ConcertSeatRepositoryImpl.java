package com.hhplus.ticketing.infrastructure.concert;

import com.hhplus.ticketing.domain.concert.entity.ConcertSeat;
import com.hhplus.ticketing.domain.concert.repository.ConcertSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ConcertSeatRepositoryImpl implements ConcertSeatRepository {

    private final ConcertSeatJpaRepository concertSeatJpaRepository;

    @Override
    public List<ConcertSeat> getConcertSeatsInfo(long concertDetailId) {
        return concertSeatJpaRepository.findByConcertDetailDetailId(concertDetailId);
    }

    @Override
    public Optional<ConcertSeat> getConcertSeatInfo(long seatId) {
        return concertSeatJpaRepository.findById(seatId);
    }

    @Override
    public ConcertSeat save(ConcertSeat concertSeat) {
        return concertSeatJpaRepository.save(concertSeat);
    }

    @Override
    public ConcertSeat findSeatByIdWithLock(Long seatId) {
        return concertSeatJpaRepository.findSeatByIdWithLock(seatId);
    }

}
