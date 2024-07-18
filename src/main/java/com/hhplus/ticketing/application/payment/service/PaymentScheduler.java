package com.hhplus.ticketing.application.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentScheduler {

    private final PaymentService paymentService;

    @Scheduled(fixedRate = 2 * 60 * 1000)
    public void checkPaymentStatus() {
        paymentService.expirePayment();
    }

}
