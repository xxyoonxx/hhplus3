package com.hhplus.ticketing.application.queue.service;

import com.hhplus.ticketing.common.exception.CustomException;
import com.hhplus.ticketing.domain.queue.QueueErrorCode;
import com.hhplus.ticketing.domain.queue.entity.Queue;
import com.hhplus.ticketing.domain.queue.repository.QueueRepository;
import com.hhplus.ticketing.presentation.queue.dto.QueueResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QueueService {

    private final QueueRepository queueRepository;

    /**
     * 대기열 진입
     * @param userId
     * @return
     */
    public QueueResponseDto enterQueue(long userId) {
        // 토큰 상태 확인 - 만료됐거나 없으면 신규 발급
        Optional<Queue> queueCheck = queueRepository.checkQueue(userId);
        Queue queue = queueCheck.orElseGet(() -> createNewQueue(userId));

        QueueResponseDto queueResponseDto = handleQueueStatus(queue);
        queueResponseDto.setQueuePosition(calculateQueuePosition(queue));
        return queueResponseDto;
    }

    /**
     * 대기열 조회
     * @param token
     * @return
     */
    public QueueResponseDto getQueueStatus(String token) {
        Queue queue = queueRepository.getTokenInfo(token)
                .orElseThrow(() -> new CustomException(QueueErrorCode.USER_NOT_FOUND));
        QueueResponseDto queueResponseDto = handleQueueStatus(queue);
        queueResponseDto.setQueuePosition(calculateQueuePosition(queue));
        return queueResponseDto;
    }

    /**
     * 토큰 신규 발급
     *
     * @param userId
     * @return
     */
    private Queue createNewQueue(long userId) {
        String token = UUID.randomUUID().toString();
        Queue queue = Queue.builder()
                .userId(userId)
                .token(token)
                .expiryDate(LocalDateTime.now().plusMinutes(30))
                .build();
        queueRepository.save(queue);
        return queue;
    }

    /**
     * 토큰 상태 확인 및 처리
     *
     * @param queue
     * @return
     */
    private QueueResponseDto handleQueueStatus(Queue queue) {
        // 토큰 만료 처리
        if (queue.getExpiryDate().isBefore(LocalDateTime.now())) {
            queue.expire();
            queueRepository.save(queue);
        }

        // 대기중이거나 예약 진행중인 토큰 존재 - 기존 토큰 반환
        if (queue.getStatus() == Queue.Status.WAITING || queue.getStatus() == Queue.Status.PROCESSING) {
            int queuePosition = calculateQueuePosition(queue);
            return QueueResponseDto.from(queue, queuePosition);
        }

        // 토큰이 만료되었거나 유효하지 않은 경우
        int queuePosition = calculateQueuePosition(queue);
        return QueueResponseDto.from(queue, queuePosition);
    }

    /**
     * 내 앞 대기인 수 계산
     * @param queue
     * @return
     */
    public int calculateQueuePosition(Queue queue) {
        Optional<Long> latestTokenIdOpt = queueRepository.getLatestTokenId();
        if (latestTokenIdOpt.isPresent()) {
            long latestTokenId = latestTokenIdOpt.get();
            return (int)(latestTokenId - queue.getTokenId() + 1);
        }
        return 0;
    }

}
