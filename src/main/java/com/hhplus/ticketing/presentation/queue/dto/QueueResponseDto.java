package com.hhplus.ticketing.presentation.queue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class QueueResponseDto {

    private String token;
    private int queuePosition;
    private int remainingtime;

    // 작성 에정
    public static QueueResponseDto from(){
        return QueueResponseDto.builder().build();
    }

}
