package com.hhplus.ticketing.infrastructure.concert;

import com.hhplus.ticketing.domain.concert.entity.ConcertSeat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConcertSeatJpaRepository extends JpaRepository<ConcertSeat, Long> {
    List<ConcertSeat> findByConcertDetailDetailId(Long concertDetailId);

    ConcertSeat findBySeatId(Long SeatId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT cs FROM ConcertSeat cs WHERE cs.id = :seatId")
    ConcertSeat findSeatByIdWithLock(@Param("seatId") Long seatId);
}
