package com.hhplus.ticketing.domain.concert.repository;

import com.hhplus.ticketing.domain.concert.entity.Concert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ConcertRepository {

    Concert save(Concert concert);
    Page<Concert> getAllConcerts(Pageable pageable);
    Concert getConcertInfo(long concertId);

}
