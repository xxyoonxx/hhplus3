package com.hhplus.ticketing.domain.userQueue.repository;

import java.util.List;

public interface RedisQueueRepository {
    void addTokenToQueue(String token, long userId);
    String getTokenByUserId(long userId);
    long getUserIdbyToken(String token);
    boolean isTokenWaiting(String token);
    boolean isTokenExpired(String token);
    int getQueuePosition(String token);
    void getTokensToActivate(int maxProcessingCount);
    List<Long> getExpiredUserInfo();
    void expireExistQueue();
    void moveQueueToActive(String token);

}
