package com.hhplus.ticketing.application.payment.service;

import com.hhplus.ticketing.application.userQueue.service.UserQueueProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentScheduler {

    private final UserQueueProcessService userQueueProcessService;

    @Scheduled(fixedRate = 2 * 60 * 1000)
    public void checkPaymentStatus() {
        userQueueProcessService.expirePayment();
    }

}
