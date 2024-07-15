package com.hhplus.ticketing.application.userQueue.service;

import com.hhplus.ticketing.common.exception.CustomException;
import com.hhplus.ticketing.domain.userQueue.UserQueueErrorCode;
import com.hhplus.ticketing.domain.userQueue.entity.UserQueue;
import com.hhplus.ticketing.domain.userQueue.repository.UserQueueRepository;
import com.hhplus.ticketing.presentation.queue.dto.UserQueueResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserQueueService {

    private final UserQueueRepository userQueueRepository;

    /**
     * 대기열 진입
     * @param userId
     * @return
     */
    public UserQueueResponseDto enterQueue(long userId) {
        // 토큰 상태 확인 - 만료됐거나 없으면 신규 발급
        Optional<UserQueue> queueCheck = userQueueRepository.checkQueue(userId);
        UserQueue userQueue = queueCheck.orElseGet(() -> createNewQueue(userId));

        UserQueueResponseDto userQueueResponseDto = handleQueueStatus(userQueue);
        userQueueResponseDto.setQueuePosition(calculateQueuePosition(userQueue));
        return userQueueResponseDto;
    }

    /**
     * 대기열 조회
     * @param token
     * @return
     */
    public UserQueueResponseDto getQueueStatus(String token) {
        UserQueue userQueue = userQueueRepository.getTokenInfo(token)
                .orElseThrow(() -> new CustomException(UserQueueErrorCode.USER_NOT_FOUND));
        UserQueueResponseDto userQueueResponseDto = handleQueueStatus(userQueue);
        userQueueResponseDto.setQueuePosition(calculateQueuePosition(userQueue));
        return userQueueResponseDto;
    }

    /**
     * 토큰 신규 발급
     *
     * @param userId
     * @return
     */
    private UserQueue createNewQueue(long userId) {
        String token = UUID.randomUUID().toString();
        UserQueue userQueue = UserQueue.builder()
                .userId(userId)
                .token(token)
                .expiryDate(LocalDateTime.now().plusMinutes(30))
                .build();
        userQueueRepository.save(userQueue);
        return userQueue;
    }

    /**
     * 토큰 상태 확인 및 처리
     *
     * @param userQueue
     * @return
     */
    @Scheduled(fixedRate = 5000)
    private UserQueueResponseDto handleQueueStatus(UserQueue userQueue) {
        // 토큰 만료 처리
        if (userQueue.getExpiryDate().isBefore(LocalDateTime.now())) {
            userQueue.expire();
            userQueueRepository.save(userQueue);
        }

        // 대기중이거나 예약 진행중인 토큰 존재 - 기존 토큰 반환
        if (userQueue.getStatus() == UserQueue.Status.WAITING || userQueue.getStatus() == UserQueue.Status.PROCESSING) {
            int queuePosition = calculateQueuePosition(userQueue);
            return UserQueueResponseDto.from(userQueue, queuePosition);
        }

        // 토큰이 만료되었거나 유효하지 않은 경우
        int queuePosition = calculateQueuePosition(userQueue);
        return UserQueueResponseDto.from(userQueue, queuePosition);
    }

    /**
     * 내 앞 대기인 수 계산
     * @param userQueue
     * @return
     */
    public int calculateQueuePosition(UserQueue userQueue) {
        Optional<Long> latestQueueIdOpt = userQueueRepository.getLatestQueueId();
        if (latestQueueIdOpt.isPresent()) {
            long latestQueueId = latestQueueIdOpt.get();
            return (int)(latestQueueId - userQueue.getQueueId() + 1);
        }
        return 0;
    }

}
