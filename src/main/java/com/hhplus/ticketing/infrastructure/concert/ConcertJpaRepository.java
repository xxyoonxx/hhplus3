package com.hhplus.ticketing.infrastructure.concert;

import com.hhplus.ticketing.domain.concert.entity.ConcertEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertJpaRepository  extends JpaRepository<ConcertEntity, Long> {
}
