package com.hhplus.ticketing.infrastructure.queue;

import com.hhplus.ticketing.domain.queue.entity.Queue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface QueueJpaRepository extends JpaRepository<Queue, Long> {

    Optional<Queue> findByToken(String token);

    Optional<Queue> findByUserIdAndExpiryDateAfter(long userId, LocalDateTime currentDate);

    Optional<Long> findTopByStatusOrderByTokenIdDesc(Queue.Status status);

}
