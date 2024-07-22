package com.hhplus.ticketing.domain.userQueue.repository;

import com.hhplus.ticketing.domain.userQueue.entity.UserQueue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserQueueRepository {
    Optional<UserQueue> getTokenInfo(String token);
    Optional<UserQueue> checkQueue(long userId, LocalDateTime currentDate);
    UserQueue save(UserQueue userQueue);
    Optional<Long> getLatestQueueId();
    List<UserQueue> getExpiredQueues(LocalDateTime currentDate);
    UserQueue getUserInfo(long userId);
    int countByStatus(UserQueue.Status status);
    List<UserQueue> enterUserQueue(int newEnterCnt);
}
