package com.hhplus.ticketing.presentation.queue;

import com.hhplus.ticketing.application.queue.service.QueueService;
import com.hhplus.ticketing.presentation.queue.dto.QueueRequestDto;
import com.hhplus.ticketing.presentation.queue.dto.QueueResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "대기열 API")
@RestController
@RequestMapping("/queue")
@RequiredArgsConstructor
public class QueueController {

    private final QueueService queueService;

    /**
     * 대기열 진입
     * @param requestDto
     * @return
     */
    @Operation(summary = "대기열 요청")
    @PostMapping("/")
    public ResponseEntity<QueueResponseDto> requestQueue(@RequestBody QueueRequestDto requestDto) {
        QueueResponseDto enterQueue = queueService.enterQueue(requestDto.getUserId());
        return ResponseEntity.ok(enterQueue);
    }

    /**
     * 대기열 조회
     * @param authorization
     * @return
     */
    @Operation(summary = "대기열 조회")
    @GetMapping("/status")
    public ResponseEntity<QueueResponseDto> queueStatus(@RequestHeader("Authorization") String authorization) {
        QueueResponseDto queueStatus = queueService.getQueueStatus(authorization);
        return ResponseEntity.ok(queueStatus);
    }

}
