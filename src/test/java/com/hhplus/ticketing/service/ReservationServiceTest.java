package com.hhplus.ticketing.service;

import com.hhplus.ticketing.domain.concert.entity.Concert;
import com.hhplus.ticketing.domain.concert.entity.ConcertDetail;
import com.hhplus.ticketing.domain.concert.entity.ConcertSeat;
import com.hhplus.ticketing.domain.concert.repository.ConcertDetailRepository;
import com.hhplus.ticketing.domain.concert.repository.ConcertRepository;
import com.hhplus.ticketing.domain.concert.repository.ConcertSeatRepository;
import com.hhplus.ticketing.domain.payment.entity.Payment;
import com.hhplus.ticketing.domain.payment.repository.PaymentRepository;
import com.hhplus.ticketing.domain.reservation.entity.Reservation;
import com.hhplus.ticketing.domain.reservation.repository.ReservationRepository;
import com.hhplus.ticketing.application.reservation.service.ReservationService;
import com.hhplus.ticketing.presentation.reservation.dto.ReservationRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ConcertSeatRepository concertSeatRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ConcertDetailRepository concertDetailRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ConcertRepository concertRepository;

    private ConcertSeat seat01;
    private ConcertDetail concertDetail;
    private Concert concert;
    private Reservation reservation;
    private Payment payment;

    @BeforeEach
    public void setUp() {
        concert = Concert.builder()
                .concertId(1L)
                .title("콘서트01")
                .build();
        concertDetail = ConcertDetail.builder()
                .detailId(1L)
                .concertDate(LocalDateTime.of(2024, 10, 1, 18, 0, 0))
                .concert(concert)
                .build();
        seat01 = ConcertSeat.builder()
                .seatId(1L)
                .seatNo("A01")
                .seatPrice(10000)
                .status(ConcertSeat.Status.AVAILABLE)
                .concertDetail(concertDetail)
                .build();
        reservation = Reservation.builder()
                .reservationId(1L)
                .concertSeat(seat01)
                .status(Reservation.Status.WAITING)
                .build();
    }

    @Test
    @DisplayName("좌석 예약")
    public void reserveSeat() {
        ReservationRequestDto dto = ReservationRequestDto.builder()
                .userId(1L)
                .detailId(1L)
                .seatId(1L)
                .reservationDate(LocalDateTime.now())
                .totalPrice(10000)
                .build();

        when(concertSeatRepository.getConcertSeatInfo(1L)).thenReturn(seat01);
        when(concertDetailRepository.getConcertInfoByDetailId(1L)).thenReturn(concertDetail);
        when(concertRepository.getConcertInfo(1L)).thenReturn(concert);
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Reservation reserved = reservationService.reserveSeat(dto);

        assertNotNull(reserved);
        assertEquals(1L, reserved.getUserId());
        assertEquals("콘서트01", reserved.getConcertTitle());
        assertEquals(10000, reserved.getTotalPrice());
        assertEquals(Reservation.Status.WAITING, reserved.getStatus());
    }

}
