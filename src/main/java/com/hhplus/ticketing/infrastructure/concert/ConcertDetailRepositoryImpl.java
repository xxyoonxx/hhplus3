package com.hhplus.ticketing.infrastructure.concert;

import com.hhplus.ticketing.domain.concert.entity.ConcertDetail;
import com.hhplus.ticketing.domain.concert.repository.ConcertDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConcertDetailRepositoryImpl implements ConcertDetailRepository {

    private final ConcertDetailJpaRepository concertDetailJpaRepository;

    @Override
    public List<ConcertDetail> getConcertDetailInfo(long concertId) {
        return concertDetailJpaRepository.findByConcertConcertId(concertId);
    }

    @Override
    public ConcertDetail getConcertInfoByDetailId(long concertDetailId) {
        return concertDetailJpaRepository.findConcertByDetailId(concertDetailId);
    }


}
