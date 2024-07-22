package com.hhplus.ticketing.domain.userQueue.repository;

import com.hhplus.ticketing.domain.userQueue.entity.UserQueue;

import java.util.Optional;

public interface UserQueueRepository {
    Optional<UserQueue> getTokenInfo(String token);
    Optional<UserQueue> checkQueue(long userId);
    UserQueue save(UserQueue userQueue);
    Optional<Long> getLatestQueueId();
    Optional<UserQueue> getUserIdByToken(String token);
}
