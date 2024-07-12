package com.hhplus.ticketing.application.queue.service;

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
     * 토큰 상태 확인
     * @param token
     * @return
     */
    public Optional<QueueResponseDto> getQueueStatus(String token) {
        Optional<Queue> queue = queueRepository.getTokenInfo(token);
        return handleQueueStatus(queue);
    }

    /**
     * 토큰 신규 발급
     * @param userId
     * @return
     */
    public QueueResponseDto enterQueue(long userId) {
        // 토큰 상태 확인 - 만료됐거나 없으면 신규 발급
        Optional<Queue> queueCheck = queueRepository.checkQueue(userId);

        if (queueCheck.isPresent()) {
            Optional<QueueResponseDto> queueResponseOpt = handleQueueStatus(queueCheck);
            return queueResponseOpt.get();
        }

        // 토큰 신규발급
        String token = UUID.randomUUID().toString();
        Queue queue = Queue.builder()
                .userId(userId)
                .token(token)
                .expiryDate(LocalDateTime.now().plusMinutes(30))
                .build();
        queueRepository.save(queue);

        int queuePosition = calculateQueuePosition(queue);
        return QueueResponseDto.from(queue, queuePosition);
    }

    /**
     * 토큰 상태 확인 및 처리
     * @param queueOpt
     * @return
     */
    private Optional<QueueResponseDto> handleQueueStatus(Optional<Queue> queueOpt) {
        if (queueOpt.isPresent()) {
            Queue queue = queueOpt.get();
            // 토큰 만료 처리
            if (queue.getExpiryDate().isBefore(LocalDateTime.now())) {
                queue.expire();
                queueRepository.save(queue);
            }

            // 대기중이거나 예약 진행중인 토큰 존재 - 기존 토큰 반환
            if (queue.getStatus() == Queue.Status.WAITING || queue.getStatus() == Queue.Status.PROCESSING) {
                int queuePosition = calculateQueuePosition(queue);
                return Optional.of(QueueResponseDto.from(queue, queuePosition));
            }
        }
        return Optional.empty();
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
