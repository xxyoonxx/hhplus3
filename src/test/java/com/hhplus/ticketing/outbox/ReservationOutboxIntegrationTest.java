package com.hhplus.ticketing.outbox;

import com.hhplus.ticketing.application.reservation.service.ReservationService;
import com.hhplus.ticketing.domain.outbox.entity.Outbox;
import com.hhplus.ticketing.domain.outbox.repository.OutboxRepository;
import com.hhplus.ticketing.presentation.reservation.dto.ReservationRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReservationOutboxIntegrationTest {

    @LocalServerPort
    protected int port;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private OutboxRepository outboxRepository;

    @Test
    @DisplayName("아웃박스 로직 테스트 - 예약")
    void reservationOutboxTest() {
        // given
        ReservationRequestDto reservationRequestDto = ReservationRequestDto.builder()
                .detailId(1L)
                .seatId(1L)
                .userId(1L)
                .totalPrice(10000)
                .reservationDate(LocalDateTime.now())
                .build();

        // when
        reservationService.reserveSeat(reservationRequestDto);

        // then
        Outbox message = outboxRepository.findByMessageAndDomain("1", Outbox.Domain.RESERVATION);
        assertNotNull(message);
        assertEquals(Outbox.Type.INIT, message.getType());
        assertEquals(Outbox.Domain.RESERVATION, message.getDomain());
    }

}
