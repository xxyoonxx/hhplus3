package com.hhplus.ticketing.presentation.queue;

import com.hhplus.ticketing.presentation.queue.dto.QueueRequestDto;
import com.hhplus.ticketing.presentation.queue.dto.QueueResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "대기열 API")
@RestController
@RequestMapping("/queue")
@RequiredArgsConstructor
public class QueueController {

    /**
     * 대기열 요청
     * @param requestDto
     * @return
     */
    @Operation(summary = "대기열 요청")
    @PostMapping("/")
    public QueueResponseDto requestQueue(@RequestBody QueueRequestDto requestDto) {
        return new QueueResponseDto("dG9rZW5WYWx1ZQ==",100, 3000);
    }

    /**
     * 대기열 조회
     * @param request
     * @return
     */
    @Operation(summary = "대기열 조회")
    @GetMapping("/status")
    public QueueResponseDto queueStatus(@RequestBody HttpServletRequest request) {
        return new QueueResponseDto("dG9rZW5WYWx1ZQ==",100, 3000);
    }

}
