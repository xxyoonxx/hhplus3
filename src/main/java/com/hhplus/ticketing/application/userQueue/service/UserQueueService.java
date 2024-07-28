package com.hhplus.ticketing.application.userQueue.service;

import com.hhplus.ticketing.common.exception.CustomException;
import com.hhplus.ticketing.domain.reservation.entity.Reservation;
import com.hhplus.ticketing.domain.userQueue.UserQueueErrorCode;
import com.hhplus.ticketing.domain.userQueue.entity.UserQueue;
import com.hhplus.ticketing.domain.userQueue.repository.UserQueueRepository;
import com.hhplus.ticketing.presentation.queue.dto.UserQueueResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserQueueService {

    private final UserQueueRepository userQueueRepository;
    private final UserQueueProcessService userQueueProcessService;

    int maxProcessingCount = 10;

    /**
     * 대기열 진입
     * @param userId
     * @return
     */
    @Transactional
    public UserQueueResponseDto enterUserQueue(long userId) {
        // 대기열 존재 확인 - 만료됐거나 없으면 신규 발급
        LocalDateTime currentDate = LocalDateTime.now();
        UserQueue userQueue  = userQueueRepository.checkQueue(userId, currentDate).orElseGet(()->createNewQueue(userId));
        return getQueueStatus(userQueue.getToken());
    }

    /**
     * 토큰 신규 발급
     * @param userId
     * @return
     */
    public UserQueue createNewQueue(long userId) {
        String token = UUID.randomUUID().toString();
        UserQueue userQueue = UserQueue.builder()
                .userId(userId)
                .token(token)
                .status(UserQueue.Status.WAITING)
                .createdDate(LocalDateTime.now())
                .expiryDate(LocalDateTime.now().plusMinutes(30))
                .build();
        userQueueRepository.save(userQueue);
        return userQueue;
    }

    /**
     * 대기열 조회 및 순번 반환(폴링)
     * @param authorization
     * @return
     */
    @Transactional
    public UserQueueResponseDto getQueueStatus(String authorization) {
        UserQueue userQueue = userQueueRepository.getTokenInfo(authorization)
                .orElseThrow(() -> new CustomException(UserQueueErrorCode.QUEUE_NOT_FOUND));

        // 'WAITING'이 아닌 대기열 예외 반환
        if (userQueue.getStatus() != UserQueue.Status.WAITING) {
            throw new CustomException(UserQueueErrorCode.QUEUE_NOT_FOUND);
        }

        // 만료일시가 지난 대기열 예외 반환
        if(userQueue.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new CustomException(UserQueueErrorCode.TOKEN_EXPIRED);
        }

        // 대기순번 조회
        Long latestQueueIdOpt = userQueueRepository.getLatestQueueId().orElse(0L);
        int queuePosition = (int)(userQueue.getQueueId() - latestQueueIdOpt);
        if (userQueue.getQueueId().equals(latestQueueIdOpt)) queuePosition = 1;

        int processedQueueCnt = userQueueRepository.countByStatus(UserQueue.Status.PROCESSING);
        if (maxProcessingCount-processedQueueCnt > 0 && maxProcessingCount > queuePosition) userQueue.changeStatus(UserQueue.Status.PROCESSING);

        return UserQueueResponseDto.from(userQueue, queuePosition);
    }

    /**
     * 만료일시 지난 대기열들 만료 처리 및 신규 대기열 진입(스케쥴러)
     */
    @Transactional
    public void expireQueues() {
        // 만료일시가 지난 대기열 만료 처리
        LocalDateTime currentDate = LocalDateTime.now();
        List<UserQueue> expiredUsers = userQueueRepository.getExpiredQueues(currentDate);

        // 만료된 인원 처리
        if (expiredUsers != null && !expiredUsers.isEmpty()) {
            expiredUsers.forEach(userQueue -> {
                userQueueProcessService.expireQueue(userQueue.getUserId(), Reservation.Status.EXPIRED);
                userQueueRepository.save(userQueue); // 상태 변경 후 저장
            });
        }

        // 현재 PROCESSING 상태인 수
        int currentProcessingCount = userQueueRepository.countByStatus(UserQueue.Status.PROCESSING);
        // 들어 갈 수 있는 자리
        int availableSlots = maxProcessingCount - currentProcessingCount;

        // 남은 자리만큼 대기열 WAITING > PROCESSING
        if (availableSlots > 0) {
            List<UserQueue> newEnterUsers = userQueueRepository.enterUserQueue(availableSlots);
            newEnterUsers.forEach(userQueue -> userQueue.changeStatus(UserQueue.Status.PROCESSING));
        }
    }
}
