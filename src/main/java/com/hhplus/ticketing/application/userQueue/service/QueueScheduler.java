package com.hhplus.ticketing.application.userQueue.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueueScheduler {

    private final UserQueueService userQueueService;

    @Scheduled(fixedRate = 2 * 60 * 1000)
    public void checkQueueStatus() {
        userQueueService.expireQueues();
    }

}
