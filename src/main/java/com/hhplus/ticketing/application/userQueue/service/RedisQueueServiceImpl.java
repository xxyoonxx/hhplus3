package com.hhplus.ticketing.application.userQueue.service;

import com.hhplus.ticketing.common.exception.CustomException;
import com.hhplus.ticketing.domain.reservation.entity.Reservation;
import com.hhplus.ticketing.domain.userQueue.UserQueueErrorCode;
import com.hhplus.ticketing.domain.userQueue.entity.UserQueue;
import com.hhplus.ticketing.domain.userQueue.repository.RedisQueueRepository;
import com.hhplus.ticketing.presentation.queue.dto.UserQueueResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RedisQueueServiceImpl implements QueueService {

    private final RedisQueueRepository redisQueueRepository;
    private final UserQueueProcessService userQueueProcessService;

    int maxProcessingCount = 10;

    /**
     * 대기열 진입
     * @param userId
     */
    public UserQueueResponseDto enterUserQueue(long userId) {
        UserQueue userQueue;
        // 대기열 존재 확인 - 만료됐거나 없으면 신규 발급
        String token = redisQueueRepository.getTokenByUserId(userId);
        if(token != null) {
            userQueue = getUserQueue(token, userId);
        } else {
            userQueue = createNewQueue(userId);
            redisQueueRepository.addTokenToQueue(userQueue.getToken(), userId);
        }
        return getQueueStatus(userQueue.getToken());
    }

    /**
     * 토큰 신규 발급
     * @param userId
     * @return
     */
    public UserQueue createNewQueue(long userId) {
        String token = UUID.randomUUID().toString();
        return getUserQueue(token, userId);
    }

    /**
     * 대기열 조회 및 순번 반환(폴링)
     * @param token
     * @return
     */
    @Transactional
    public UserQueueResponseDto getQueueStatus(String token) {
        long userId = redisQueueRepository.getUserIdbyToken(token);
        UserQueue userQueue = getUserQueue(token, userId);

        // 'WAITING'에 없는 대기열 예외 반환
        if (!redisQueueRepository.isTokenWaiting(token)) {
            throw new CustomException(UserQueueErrorCode.QUEUE_NOT_FOUND);
        }

        // 만료일시가 지난 대기열 예외 반환
        if(redisQueueRepository.isTokenExpired(token)) {
            throw new CustomException(UserQueueErrorCode.TOKEN_EXPIRED);
        }

        // 대기순번 조회
        int queuePosition = getQueuePosition(token);

        return UserQueueResponseDto.from(userQueue, queuePosition);
    }

    /**
     * 대기열 조회 및 순번 반환
     * @param token
     * @return
     */
    public int getQueuePosition(String token) {
        Integer queuePosition = redisQueueRepository.getQueuePosition(token);
        if(queuePosition == null) throw new CustomException(UserQueueErrorCode.QUEUE_NOT_FOUND);
        return queuePosition;
    }

    /**
     * 대기열 만료 처리 및 참가열 진입(스케쥴러)
     */
    public void expireQueues() {
        redisQueueRepository.getTokensToActivate(maxProcessingCount);
        List<Long> userIds = redisQueueRepository.getExpiredUserInfo();
        // 만료된 인원 처리
        if (userIds != null && !userIds.isEmpty()) {
            userIds.forEach(userId -> userQueueProcessService.expireQueue(userId, Reservation.Status.EXPIRED));
        }
    }

    /**
     * 대기열 검증
     * @param token
     * @return
     */
    @Override
    public UserQueue validateToken(String token) {
        long userId = redisQueueRepository.getUserIdbyToken(token);
        UserQueue userQueue = getUserQueue(token, userId);
        if(redisQueueRepository.isTokenWaiting(token)) throw new CustomException(UserQueueErrorCode.NOT_IN_QUEUE);
        if(redisQueueRepository.isTokenExpired(token)) throw new CustomException(UserQueueErrorCode.TOKEN_EXPIRED);
        return userQueue;
    }

    /**
     * Entity 객체 get
     * @param token
     * @param userId
     * @return
     */
    private UserQueue getUserQueue(String token, long userId) {
        return UserQueue.builder()
                .userId(userId)
                .token(token)
                .status(UserQueue.Status.WAITING)
                .createdDate(LocalDateTime.now())
                .expiryDate(LocalDateTime.now().plusMinutes(30))
                .build();
    }

}
