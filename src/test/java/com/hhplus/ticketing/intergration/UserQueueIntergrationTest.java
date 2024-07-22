package com.hhplus.ticketing.intergration;

import com.hhplus.ticketing.application.userQueue.service.UserQueueProcessService;
import com.hhplus.ticketing.application.userQueue.service.UserQueueService;
import com.hhplus.ticketing.common.exception.CustomException;
import com.hhplus.ticketing.domain.userQueue.UserQueueErrorCode;
import com.hhplus.ticketing.domain.userQueue.entity.UserQueue;
import com.hhplus.ticketing.domain.userQueue.repository.UserQueueRepository;
import com.hhplus.ticketing.presentation.queue.dto.UserQueueResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserQueueIntergrationTest {

    @Autowired
    private UserQueueService userQueueService;

    @Autowired
    private UserQueueRepository userQueueRepository;

    @Autowired
    private UserQueueProcessService userQueueProcessService;

    private LocalDateTime currentDate;

    @BeforeEach
    void setUp() {
        currentDate = LocalDateTime.now();

        UserQueue validQueue = UserQueue.builder()
                .userId(1L)
                .token("validToken")
                .expiryDate(currentDate.plusMinutes(30))
                .status(UserQueue.Status.WAITING)
                .build();

        UserQueue expiredQueue = UserQueue.builder()
                .userId(2L)
                .token("expired-token")
                .expiryDate(currentDate.minusMinutes(1))
                .status(UserQueue.Status.WAITING)
                .build();

        UserQueue processedQueue = UserQueue.builder()
                .userId(3L)
                .token("processed-token")
                .expiryDate(currentDate.plusMinutes(15))
                .status(UserQueue.Status.PROCESSING)
                .build();

        userQueueRepository.save(validQueue);
        userQueueRepository.save(expiredQueue);
        userQueueRepository.save(processedQueue);
    }

    @Test
    @DisplayName("대기열 진입")
    void testEnterUserQueue() {
        // given
        long userId = 4L;

        // when
        UserQueueResponseDto responseDto = userQueueService.enterUserQueue(userId);

        // then
        assertNotNull(responseDto);
        assertEquals(UserQueue.Status.WAITING, responseDto.getStatus());
        assertTrue(responseDto.getQueuePosition() >= 0);
    }

    @Test
    @DisplayName("대기열 상태 조회")
    void testGetQueueStatus_ValidToken() {
        // given
        String validToken = "validToken";

        // when
        UserQueueResponseDto responseDto = userQueueService.getQueueStatus(validToken);

        // then
        assertEquals(validToken, responseDto.getToken());
        assertEquals(UserQueue.Status.WAITING, responseDto.getStatus());
    }

    @Test
    @DisplayName("대기열 상태 조회 - 만료일자 지남")
    void GetQueueStatusException() {
        // 대기열 만료
        String expiredToken = "expired-token";
        CustomException exception = assertThrows(CustomException.class, () -> userQueueService.getQueueStatus(expiredToken));
        assertEquals(UserQueueErrorCode.TOKEN_EXPIRED, exception.getErrorCode());

        // WAITING 상태 아님
        String processedToken = "processed-token";
        exception = assertThrows(CustomException.class, () -> userQueueService.getQueueStatus(processedToken));
        assertEquals(UserQueueErrorCode.QUEUE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("대기열 상태 변경")
    void expireQueues() {
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

        userQueueRepository.save(expiredQueue1);
        userQueueRepository.save(expiredQueue2);

        UserQueue newQueue1 = UserQueue.builder()
                .userId(3L)
                .status(UserQueue.Status.WAITING)
                .build();

        UserQueue newQueue2 = UserQueue.builder()
                .userId(4L)
                .status(UserQueue.Status.WAITING)
                .build();

        userQueueRepository.save(newQueue1);
        userQueueRepository.save(newQueue2);

        // when
        userQueueService.expireQueues();

        // then
        UserQueue updatedNewQueue1 = userQueueRepository.getUserInfo(newQueue1.getQueueId());
        UserQueue updatedNewQueue2 = userQueueRepository.getUserInfo(newQueue2.getQueueId());

        assertEquals(UserQueue.Status.PROCESSING, updatedNewQueue1.getStatus());
        assertEquals(UserQueue.Status.PROCESSING, updatedNewQueue2.getStatus());
    }
}