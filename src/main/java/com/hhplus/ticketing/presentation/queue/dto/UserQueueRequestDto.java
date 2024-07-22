package com.hhplus.ticketing.presentation.queue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class UserQueueRequestDto {

    private long userId;

}
