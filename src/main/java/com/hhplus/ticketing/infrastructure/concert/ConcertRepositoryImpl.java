package com.hhplus.ticketing.infrastructure.concert;

import com.hhplus.ticketing.domain.concert.entity.Concert;
import com.hhplus.ticketing.domain.concert.repository.ConcertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertJpaRepository concertJpaRepository;

    @Override
    public Concert save(Concert concert) {
        return concertJpaRepository.save(concert);
    }

    @Override
    public Page<Concert> getAllConcerts(Pageable pageable) {
        return concertJpaRepository.findAll(pageable);
    }

    @Override
    public Concert getConcertInfo(long concertId) {
        return concertJpaRepository.getByConcertId(concertId);
    }


}
