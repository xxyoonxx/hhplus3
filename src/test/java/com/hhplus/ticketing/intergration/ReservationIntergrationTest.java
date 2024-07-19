package com.hhplus.ticketing.intergration;

import com.hhplus.ticketing.application.reservation.service.ReservationService;
import com.hhplus.ticketing.common.exception.CustomException;
import com.hhplus.ticketing.domain.concert.entity.Concert;
import com.hhplus.ticketing.domain.concert.entity.ConcertDetail;
import com.hhplus.ticketing.domain.concert.entity.ConcertSeat;
import com.hhplus.ticketing.domain.reservation.entity.Reservation;
import com.hhplus.ticketing.presentation.reservation.dto.ReservationRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Import(TestSetupConfig.class)
public class ReservationIntergrationTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ConcertSeat seat01;

    @Autowired
    private ConcertSeat seat02;

    @Autowired
    private ConcertDetail concertDetail;

    @Autowired
    private Concert concert;

    @Autowired
    private Reservation reservation;

    @Test
    @DisplayName("좌석 예약")
    void getConcertSeat() {

        ReservationRequestDto dto = ReservationRequestDto.builder()
                .userId(1L)
                .detailId(1L)
                .seatId(1L)
                .reservationDate(LocalDateTime.now())
                .totalPrice(10000)
                .build();

        Reservation reserved = reservationService.reserveSeat(dto);

        assertNotNull(reserved);
        assertEquals(1L, reserved.getUserId());
        assertEquals("콘서트01", reserved.getConcertTitle());
        assertEquals(10000, reserved.getTotalPrice());
        assertEquals(Reservation.Status.WAITING, reserved.getStatus());

    }

    @Test
    @DisplayName("예약 불가")
    void getReservationException(){

        // 좌석 없음
        ReservationRequestDto nullSeat = ReservationRequestDto.builder()
                                    .userId(1L)
                                    .detailId(1L)
                                    .seatId(0L) // 없는 좌석
                                    .reservationDate(LocalDateTime.now())
                                    .totalPrice(10000)
                                    .build();

        CustomException ex = assertThrows(CustomException.class, ()-> reservationService.reserveSeat(nullSeat));


        assertEquals("예약 가능한 좌석이 없습니다.", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getErrorCode());

        // 좌석 예약됨
        ReservationRequestDto occupiedSeat = ReservationRequestDto.builder()
                                        .userId(1L)
                                        .detailId(1L)
                                        .seatId(10L) // OCCUPIED 된 좌석
                                        .reservationDate(LocalDateTime.now())
                                        .totalPrice(10000)
                                        .build();

        ex = assertThrows(CustomException.class, ()-> reservationService.reserveSeat(occupiedSeat));

        assertEquals("예약 가능한 좌석이 없습니다.", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getErrorCode());

    }


}
