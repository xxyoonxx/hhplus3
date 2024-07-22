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
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserQueueService {

    private final UserQueueRepository userQueueRepository;
    private final UserQueueProcessService userQueueProcessService;

    /**
     * 대기열 진입
     * @param userId
     * @return
     */
    public UserQueueResponseDto enterUserQueue(long userId) {
        // 대기열 존재 확인 - 만료됐거나 없으면 신규 발급
        LocalDateTime currentDate = LocalDateTime.now();
        Optional<UserQueue> optionalUserQueue  = userQueueRepository.checkQueue(userId, currentDate);

        UserQueue userQueue;
        if (optionalUserQueue.isPresent()) {
            userQueue = optionalUserQueue.get();
        } else {
            userQueue = createNewQueue(userId);
        }

        int queuePosition = calculateQueuePosition(userQueue);
        if (queuePosition == 0) userQueue.changeStatus(UserQueue.Status.PROCESSING);

        return UserQueueResponseDto.from(userQueue, queuePosition);
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

        // 대기순번
        int queuePosition = calculateQueuePosition(userQueue);
        if (queuePosition == 0) userQueue.changeStatus(UserQueue.Status.PROCESSING);
        return UserQueueResponseDto.from(userQueue, queuePosition);
    }

    /**
     * 내 앞 대기인 수 계산 및 대기열 입장 처리
     * @param userQueue
     * @return
     */
    public int calculateQueuePosition(UserQueue userQueue) {
        // 가장 최근 PROCESSING에 진입한 QueueID 조회
        Long latestQueueIdOpt = userQueueRepository.getLatestQueueId().orElse(0L);
        if (userQueue.getQueueId().equals(latestQueueIdOpt)) return 0;
        return (int)(latestQueueIdOpt - userQueue.getQueueId());
    }

    /**
     * 만료일시 지난 대기열들 만료 처리 및 신규 대기열 진입
     * 최대 PROCESSING 인원 10명
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

        int maxProcessingCount = 10;
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
