package com.hhplus.ticketing.presentation.queue.dto;

import com.hhplus.ticketing.domain.queue.entity.Queue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Data
public class QueueResponseDto {

    private String token;
    private int queuePosition;
    private int remainingTime;
    private Queue.Status status;

    public static QueueResponseDto from(Queue queue, int queuePosition){
        int remainingTime = (int) Duration.between(LocalDateTime.now(), queue.getExpiryDate()).getSeconds();

        return QueueResponseDto.builder()
                .token(queue.getToken())
                .queuePosition(queuePosition)
                .remainingTime(remainingTime)
                .status(queue.getStatus())
                .build();
    }

}
