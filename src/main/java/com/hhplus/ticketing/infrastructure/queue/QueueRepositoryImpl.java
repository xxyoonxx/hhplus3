package com.hhplus.ticketing.infrastructure.queue;

import com.hhplus.ticketing.domain.queue.entity.Queue;
import com.hhplus.ticketing.domain.queue.repository.QueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QueueRepositoryImpl implements QueueRepository {

    private final QueueJpaRepository queueJpaRepository;

    @Override
    public Optional<Queue> getTokenInfo(String token) {
        return queueJpaRepository.findByToken(token);
    }

    @Override
    public Optional<Queue> checkQueue(long userId) {
        LocalDateTime currentDate = LocalDateTime.now();
        return queueJpaRepository.findByUserIdAndExpiryDateAfter(userId, currentDate);
    }

    @Override
    public Queue save(Queue queue) {
        return queueJpaRepository.save(queue);
    }

    @Override
    public Optional<Long> getLatestTokenId() {
        return queueJpaRepository.findTopByStatusOrderByTokenIdDesc(Queue.Status.PROCESSING);
    }

    @Override
    public Optional<Queue> getUserIdByToken(String token) {
        return queueJpaRepository.findByToken(token);
    }
}
