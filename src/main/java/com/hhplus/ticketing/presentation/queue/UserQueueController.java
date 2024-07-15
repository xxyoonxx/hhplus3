package com.hhplus.ticketing.presentation.queue;

import com.hhplus.ticketing.application.userQueue.service.UserQueueService;
import com.hhplus.ticketing.presentation.queue.dto.UserQueueRequestDto;
import com.hhplus.ticketing.presentation.queue.dto.UserQueueResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "대기열 API")
@RestController
@RequestMapping("/queue")
@RequiredArgsConstructor
public class UserQueueController {

    private final UserQueueService userQueueService;

    /**
     * 대기열 진입
     * @param requestDto
     * @return
     */
    @Operation(summary = "대기열 요청")
    @PostMapping("/")
    public ResponseEntity<UserQueueResponseDto> requestQueue(@RequestBody UserQueueRequestDto requestDto) {
        UserQueueResponseDto enterQueue = userQueueService.enterQueue(requestDto.getUserId());
        return ResponseEntity.ok(enterQueue);
    }

    /**
     * 대기열 조회
     * @param authorization
     * @return
     */
    @Operation(summary = "대기열 조회")
    @GetMapping("/status")
    public ResponseEntity<UserQueueResponseDto> queueStatus(@RequestHeader("Authorization") String authorization) {
        UserQueueResponseDto queueStatus = userQueueService.getQueueStatus(authorization);
        return ResponseEntity.ok(queueStatus);
    }

}
