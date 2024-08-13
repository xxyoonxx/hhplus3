package com.hhplus.ticketing.infrastructure.concert;

import com.hhplus.ticketing.domain.concert.entity.Concert;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;

public interface ConcertJpaRepository  extends JpaRepository<Concert, Long> {

    Page<Concert> findAll(Pageable pageable);
    Concert getByConcertId(Long concertId);

}
