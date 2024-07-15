package com.hhplus.ticketing.service;

import com.hhplus.ticketing.domain.userQueue.entity.UserQueue;
import com.hhplus.ticketing.domain.userQueue.repository.UserQueueRepository;
import com.hhplus.ticketing.application.userQueue.service.UserQueueService;
import com.hhplus.ticketing.presentation.queue.dto.UserQueueResponseDto;
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
public class UserQueueServiceTest {

    @InjectMocks
    private UserQueueService userQueueService;

    @Mock
    private UserQueueRepository userQueueRepository;

    @Test
    @DisplayName("토큰 상태 확인")
    void GetQueueStatus() {
        String token = "test-token";
        UserQueue userQueue = UserQueue.builder()
                .userId(123L)
                .token(token)
                .status(UserQueue.Status.WAITING)
                .expiryDate(LocalDateTime.now().plusMinutes(30))
                .build();
        when(userQueueRepository.getTokenInfo(token)).thenReturn(Optional.of(userQueue));

        UserQueueResponseDto result = userQueueService.getQueueStatus(token);

        assertEquals(userQueue.getToken(), result.getToken());
        verify(userQueueRepository, times(1)).getTokenInfo(token);
    }

    @Test
    @DisplayName("대기열 진입")
    void EnterQueue() {
        long userId = 456L;
        when(userQueueRepository.checkQueue(userId)).thenReturn(Optional.empty());

        UserQueueResponseDto result = userQueueService.enterQueue(userId);

        verify(userQueueRepository, times(1)).checkQueue(userId);
        verify(userQueueRepository, times(1)).save(any(UserQueue.class));
    }

    @Test
    @DisplayName("만료 토큰 처리")
    void expireToken() {
        String expiredToken = "expired-token";
        UserQueue expiredUserQueue = UserQueue.builder()
                .userId(789L)
                .token(expiredToken)
                .status(UserQueue.Status.WAITING)
                .expiryDate(LocalDateTime.now().minusMinutes(1))
                .build();
        when(userQueueRepository.getTokenInfo(expiredToken)).thenReturn(Optional.of(expiredUserQueue));

        UserQueueResponseDto result = userQueueService.getQueueStatus(expiredToken);
        assertNotNull(result); // QueueResponseDto가 null이 아닌지 확인
        assertEquals(UserQueue.Status.EXPIRED, result.getStatus()); // 만료 상태인지 확인

        verify(userQueueRepository, times(1)).getTokenInfo(expiredToken);
        verify(userQueueRepository, times(1)).save(expiredUserQueue); // 만료된 토큰이 저장되었는지 확인
    }

    @Test
    @DisplayName("대기인수 조회")
    public void testCalculateQueuePosition() {
        when(userQueueRepository.getLatestQueueId()).thenReturn(Optional.of(11L));

        UserQueue userQueue = UserQueue.builder()
                .queueId(5L)
                .userId(123)
                .token("test-token")
                .status(UserQueue.Status.WAITING)
                .expiryDate(LocalDateTime.now().plusHours(1))
                .build();

        int position = userQueueService.calculateQueuePosition(userQueue);

        assertEquals(7, position);
    }

}
