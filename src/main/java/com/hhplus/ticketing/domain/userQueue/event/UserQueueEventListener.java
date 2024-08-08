package com.hhplus.ticketing.domain.userQueue.event;

import com.hhplus.ticketing.application.userQueue.service.UserQueueProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserQueueEventListener {

    private final UserQueueProcessService userQueueProcessService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void QueueExpireHandler(UserQueueEvent userQueueEvent){
        userQueueProcessService.expireQueue(userQueueEvent.getUserId(), userQueueEvent.getStatus());
    }

}
