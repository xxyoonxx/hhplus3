package com.hhplus.ticketing.outbox;

import com.hhplus.ticketing.application.payment.service.PaymentService;
import com.hhplus.ticketing.domain.outbox.entity.Outbox;
import com.hhplus.ticketing.domain.outbox.repository.OutboxRepository;
import com.hhplus.ticketing.presentation.payment.dto.PaymentRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserQueueOutboxIntegrationTest {

    @LocalServerPort
    protected int port;

    @Autowired
    private OutboxRepository outboxRepository;

    @Autowired
    private PaymentService paymentService;

    @Test
    @DisplayName("아웃박스 로직 테스트 - 대기열 만료")
    void userQueueOutboxTest() {
        // given
        PaymentRequestDto paymentRequestDto = PaymentRequestDto.builder()
                .reservationId(1L)
                .build();

        // when
        paymentService.createPayment(paymentRequestDto);

        // then
        Outbox message = outboxRepository.findByMessageAndDomain("1", Outbox.Domain.USERQUEUE);
        assertNotNull(message);
        assertEquals(Outbox.Type.INIT, message.getType());
        assertEquals(Outbox.Domain.USERQUEUE, message.getDomain());
    }

}
