package com.hhplus.ticketing.presentation.queue.dto;

import com.hhplus.ticketing.domain.userQueue.entity.UserQueue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Data
public class UserQueueResponseDto {

    private String token;
    private int queuePosition;
    private int remainingTime;
    private UserQueue.Status status;

    public static UserQueueResponseDto from(UserQueue userQueue, int queuePosition){
        int remainingTime = (int) Duration.between(LocalDateTime.now(), userQueue.getExpiryDate()).getSeconds();

        return UserQueueResponseDto.builder()
                .token(userQueue.getToken())
                .queuePosition(queuePosition)
                .remainingTime(remainingTime)
                .status(userQueue.getStatus())
                .build();
    }

}
