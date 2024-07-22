package com.hhplus.ticketing.infrastructure.concert;

import com.hhplus.ticketing.domain.concert.entity.Concert;
import com.hhplus.ticketing.domain.concert.repository.ConcertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertJpaRepository concertJpaRepository;

    @Override
    public List<Concert> getAllConcerts() {
        return concertJpaRepository.findAll();
    }

    @Override
    public Concert getConcertInfo(long concertId) {
        return concertJpaRepository.getByConcertId(concertId);
    }


}
