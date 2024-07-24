package com.hhplus.ticketing.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hhplus.ticketing.application.userQueue.service.UserQueueProcessService;
import com.hhplus.ticketing.application.userQueue.service.UserQueueService;
import com.hhplus.ticketing.common.exception.CustomException;
import com.hhplus.ticketing.domain.userQueue.UserQueueErrorCode;
import com.hhplus.ticketing.domain.userQueue.entity.UserQueue;
import com.hhplus.ticketing.domain.userQueue.repository.UserQueueRepository;
import com.hhplus.ticketing.presentation.queue.dto.UserQueueResponseDto;

@ExtendWith(MockitoExtension.class)
public class UserQueueServiceTest {

    @Mock
    private UserQueueRepository userQueueRepository;

    @Mock
    private UserQueueProcessService userQueueProcessService;

    @Spy
    @InjectMocks
    private UserQueueService userQueueService;

    private LocalDateTime currentDate = LocalDateTime.now();

    @Test
    @DisplayName("대기열 상태 조회")
    void testGetQueueStatus_ValidToken() {
        // given
        String validToken = "validToken";
        LocalDateTime currentDate = LocalDateTime.now();
        UserQueue validQueue = UserQueue.builder()
                .userId(1L)
                .token(validToken)
                .expiryDate(currentDate.plusMinutes(30))
                .status(UserQueue.Status.WAITING)
                .build();

        when(userQueueRepository.getTokenInfo(validToken)).thenReturn(Optional.ofNullable(validQueue));
        doReturn(10).when(userQueueService).getQueueStatus(validToken);

        // when
        UserQueueResponseDto responseDto = userQueueService.getQueueStatus(validToken);

        // then
        assertEquals(validToken, responseDto.getToken());
        assertEquals(UserQueue.Status.WAITING, responseDto.getStatus());
    }

    @Test
    @DisplayName("대기열 상태 조회 - 만료일자 지남")
    void testGetQueueStatus_ExpiredToken() {
        // given
        String expiredToken = "expired-token";
        UserQueue expiredQueue = UserQueue.builder()
                .userId(2L)
                .token(expiredToken)
                .expiryDate(currentDate.minusMinutes(1))
                .status(UserQueue.Status.WAITING)
                .build();
        when(userQueueRepository.getTokenInfo(expiredToken)).thenReturn(Optional.ofNullable(expiredQueue));

        // when
        CustomException exception = assertThrows(CustomException.class, () -> userQueueService.getQueueStatus(expiredToken));

        // then
        assertEquals(UserQueueErrorCode.TOKEN_EXPIRED, exception.getErrorCode());
    }

    @Test
    @DisplayName("대기열 상태 조회 - 상태가 WAITING이 아님")
    void testGetQueueStatus_ExpireAndRequeue() {
        // given
        String processedToken = "processed-token";
        UserQueue processedQueue = UserQueue.builder()
                .userId(2L)
                .token(processedToken)
                .expiryDate(currentDate.plusMinutes(15))
                .status(UserQueue.Status.PROCESSING)
                .build();
        when(userQueueRepository.getTokenInfo(processedToken)).thenReturn(Optional.ofNullable(processedQueue));

        // when
        CustomException exception = assertThrows(CustomException.class, () -> userQueueService.getQueueStatus(processedToken));

        // then
        assertEquals(UserQueueErrorCode.QUEUE_NOT_FOUND, exception.getErrorCode());

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

        UserQueueResponseDto position = userQueueService.getQueueStatus(userQueue.getToken());

        assertEquals(6, position.getQueuePosition());
    }

    @Test
    @DisplayName("만료된 큐 처리 및 새로운 큐 상태 변경")
    void testExpireQueues() {
        // given
        UserQueue expiredQueue1 = UserQueue.builder()
                .userId(1L)
                .expiryDate(currentDate.minusMinutes(5))
                .status(UserQueue.Status.PROCESSING)
                .build();

        UserQueue expiredQueue2 = UserQueue.builder()
                .userId(2L)
                .expiryDate(currentDate.minusMinutes(10))
                .status(UserQueue.Status.PROCESSING)
                .build();

        List<UserQueue> expiredUsers = Arrays.asList(expiredQueue1, expiredQueue2);

        UserQueue newQueue1 = UserQueue.builder()
                .userId(3L)
                .status(UserQueue.Status.WAITING)
                .build();

        UserQueue newQueue2 = UserQueue.builder()
                .userId(4L)
                .status(UserQueue.Status.WAITING)
                .build();

        List<UserQueue> newEnterUsers = Arrays.asList(newQueue1, newQueue2);

        doReturn(expiredUsers).when(userQueueRepository).getExpiredQueues(any(LocalDateTime.class));
        when(userQueueRepository.countByStatus(UserQueue.Status.PROCESSING)).thenReturn(8);
        when(userQueueRepository.enterUserQueue(2)).thenReturn(newEnterUsers);

        // when
        userQueueService.expireQueues();

        // then
        assertEquals(UserQueue.Status.PROCESSING, newQueue1.getStatus());
        assertEquals(UserQueue.Status.PROCESSING, newQueue2.getStatus());
    }

}