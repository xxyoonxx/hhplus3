package com.hhplus.ticketing.presentation.queue;

import com.hhplus.ticketing.presentation.queue.dto.QueueRequestDto;
import com.hhplus.ticketing.presentation.queue.dto.QueueResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/queue")
@RequiredArgsConstructor
public class QueueController {

    /**
     * 대기열 요청
     * @param requestDto
     * @return
     */
    @PostMapping("/")
    public QueueResponseDto requestQueue(@RequestBody QueueRequestDto requestDto) {
        return new QueueResponseDto("dG9rZW5WYWx1ZQ==",100, 3000);
    }

}
