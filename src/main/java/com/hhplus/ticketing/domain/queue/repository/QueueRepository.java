package com.hhplus.ticketing.domain.queue.repository;

import com.hhplus.ticketing.domain.queue.entity.Queue;

import java.util.Optional;

public interface QueueRepository {

    Optional<Queue> getTokenInfo(String token);
    Optional<Queue> checkQueue(long userId);
    Queue save(Queue queue);
    Optional<Long> getLatestTokenId();
    Optional<Queue> getUserIdByToken(String token);
}
