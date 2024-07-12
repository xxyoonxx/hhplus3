package com.hhplus.ticketing.infrastructure.concert;

import com.hhplus.ticketing.domain.concert.entity.ConcertDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcertDetailJpaRepository extends JpaRepository<ConcertDetail, Long> {
    List<ConcertDetail> findByConcertConcertId(Long concertId);
    ConcertDetail findConcertByDetailId(Long detailId);
}
