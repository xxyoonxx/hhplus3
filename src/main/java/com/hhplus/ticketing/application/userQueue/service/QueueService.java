package com.hhplus.ticketing.application.userQueue.service;

import com.hhplus.ticketing.domain.userQueue.entity.UserQueue;
import com.hhplus.ticketing.presentation.queue.dto.UserQueueResponseDto;

public interface QueueService {

    UserQueueResponseDto enterUserQueue(long userId);
    UserQueue createNewQueue(long userId);
    UserQueueResponseDto getQueueStatus(String authorization);
    void expireQueues();
    UserQueue validateToken(String authorization);

}
