package com.hhplus.ticketing.infrastructure.userQueue;

import com.hhplus.ticketing.domain.userQueue.repository.RedisQueueRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisQueueRepositoryImpl implements RedisQueueRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private ZSetOperations<String, String> zSetOperations;

    @PostConstruct
    public void init() {
        zSetOperations = redisTemplate.opsForZSet();
    }

    // 입장 토큰 발급
    public void addTokenToQueue(String token, long userId) {
        zSetOperations.add("WAITING", token, System.currentTimeMillis());
        redisTemplate.opsForHash().put("QUEUE_USER", userId, token);
    }

    // userId로 token값 조회
    public String getTokenByUserId(long userId) {
        return (String) redisTemplate.opsForHash().get("QUEUE_USER", userId);
    }

    // token값으로 unserId 조회
    public long getUserIdbyToken(String token){
        return (Long) redisTemplate.opsForHash().get("QUEUE_USER", token);
    }

    // WAITING - 토큰 여부 조회
    public boolean isTokenWaiting(String token) {
        Double score = zSetOperations.score("WAITING", token);
        return score != null;
    }

    // 토큰 만료 여부 조회
    public boolean isTokenExpired(String token) {
        Double createdTime = zSetOperations.score("WAITING", token);
        if (createdTime == null) return true;
        long currentTime = System.currentTimeMillis();
        return currentTime - createdTime > TimeUnit.MINUTES.toMillis(30);
    }

    // 대기열 순서 확인
    public int getQueuePosition(String token) {
        return zSetOperations.rank("WAITING", token).intValue();
    }

    // activate할 토큰 조회
    public void getTokensToActivate(int maxProcessingCount) {
        long currentTimeMillis = System.currentTimeMillis();
        long validTimeRange = currentTimeMillis - TimeUnit.MINUTES.toMillis(30);
        // 1. 만료시간이 지나지 않았고 2. 먼저 진입한 순으로 maxProcessingCount만큼 입장
        Set<String> tokens = zSetOperations.rangeByScore("WAITING", validTimeRange, currentTimeMillis, 0, maxProcessingCount);
        if (tokens != null) {
            for (String token : tokens) {
                expireExistQueue();
                moveQueueToActive(token);
            }
        }
    }

    // 만료 유저 정보 반환
    public List<Long> getExpiredUserInfo() {
        Set<String> activeTokens = redisTemplate.opsForSet().members("ACTIVATE");
        List<Long> userIds = new ArrayList<>();
        if (activeTokens != null) {
            for (String token : activeTokens) {
                userIds.add((Long) redisTemplate.opsForHash().get("QUEUE_USER", token));
            }
        }
        return userIds;
    }

    // 기존 참가열 만료
    public void expireExistQueue(){
        redisTemplate.delete("ACTIVATE");
    }

    // 신규 참가열 입장
    public void moveQueueToActive(String token) {
        redisTemplate.opsForSet().add("ACTIVATE", token);
        zSetOperations.remove("WAITING", token);
    }

}
