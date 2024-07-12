package com.hhplus.ticketing.service;

import com.hhplus.ticketing.domain.queue.entity.Queue;
import com.hhplus.ticketing.domain.queue.repository.QueueRepository;
import com.hhplus.ticketing.application.queue.service.QueueService;
import com.hhplus.ticketing.presentation.queue.dto.QueueResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QueueServiceTest {

    @InjectMocks
    private QueueService queueService;

    @Mock
    private QueueRepository queueRepository;

    private Queue queue;

    @Test
    @DisplayName("토큰 상태 확인")
    void GetQueueStatus() {
        String token = "test-token";
        Queue queue = Queue.builder()
                .userId(123L)
                .token(token)
                .status(Queue.Status.WAITING)
                .expiryDate(LocalDateTime.now().plusMinutes(30))
                .build();
        when(queueRepository.getTokenInfo(token)).thenReturn(Optional.of(queue));

        QueueResponseDto result = queueService.getQueueStatus(token);

        assertEquals(queue.getToken(), result.getToken());
        verify(queueRepository, times(1)).getTokenInfo(token);
    }

    @Test
    @DisplayName("대기열 진입")
    void EnterQueue() {
        long userId = 456L;
        when(queueRepository.checkQueue(userId)).thenReturn(Optional.empty());

        QueueResponseDto result = queueService.enterQueue(userId);

        verify(queueRepository, times(1)).checkQueue(userId);
        verify(queueRepository, times(1)).save(any(Queue.class));
    }

    @Test
    @DisplayName("만료 토큰 처리")
    void expireToken() {
        String expiredToken = "expired-token";
        Queue expiredQueue = Queue.builder()
                .userId(789L)
                .token(expiredToken)
                .status(Queue.Status.WAITING)
                .expiryDate(LocalDateTime.now().minusMinutes(1))
                .build();
        when(queueRepository.getTokenInfo(expiredToken)).thenReturn(Optional.of(expiredQueue));

        QueueResponseDto result = queueService.getQueueStatus(expiredToken);
        assertNotNull(result); // QueueResponseDto가 null이 아닌지 확인
        assertEquals(Queue.Status.EXPIRED, result.getStatus()); // 만료 상태인지 확인

        verify(queueRepository, times(1)).getTokenInfo(expiredToken);
        verify(queueRepository, times(1)).save(expiredQueue); // 만료된 토큰이 저장되었는지 확인
    }

    @Test
    @DisplayName("대기인수 조회")
    public void testCalculateQueuePosition() {
        when(queueRepository.getLatestTokenId()).thenReturn(Optional.of(11L));

        queue = Queue.builder()
                .tokenId(5L)
                .userId(123)
                .token("test-token")
                .status(Queue.Status.WAITING)
                .expiryDate(LocalDateTime.now().plusHours(1))
                .build();

        int position = queueService.calculateQueuePosition(queue);

        assertEquals(7, position);
    }

}
