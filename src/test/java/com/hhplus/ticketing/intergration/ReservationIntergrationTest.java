package com.hhplus.ticketing.intergration;

import com.hhplus.ticketing.application.reservation.service.ReservationService;
import com.hhplus.ticketing.common.exception.CustomException;
import com.hhplus.ticketing.domain.concert.entity.ConcertSeat;
import com.hhplus.ticketing.domain.reservation.entity.Reservation;
import com.hhplus.ticketing.infrastructure.concert.ConcertSeatJpaRepository;
import com.hhplus.ticketing.presentation.reservation.dto.ReservationRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ReservationIntergrationTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ConcertSeatJpaRepository concertSeatJpaRepository;

    @Test
    @DisplayName("동시성 테스트 - 예약")
    void reservationConcurrencyTest() throws InterruptedException {

        final int threadCount = 10;
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger fail = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            int finalI = i;
            executorService.execute(() -> {
                try {
                    ReservationRequestDto reservationRequestDto = ReservationRequestDto.builder()
                            .detailId(1L)
                            .seatId(1L)
                            .userId(finalI)
                            .totalPrice(10000)
                            .reservationDate(LocalDateTime.now())
                            .build();
                    reservationService.reserveSeat(reservationRequestDto);
                    success.incrementAndGet();
                } catch(Exception e) {
                    System.err.println(e.getMessage());
                    fail.incrementAndGet();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        Thread.sleep(1000);

        ConcertSeat concertSeat = concertSeatJpaRepository.findBySeatId(1L);
        assertEquals(1, success.get());
        assertEquals(threadCount-1, fail.get());
    }


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
                                    .reservationId(1)
                                    .userId(1L)
                                    .detailId(1L)
                                    .seatId(0L) // 없는 좌석
                                    .reservationDate(LocalDateTime.now())
                                    .totalPrice(10000)
                                    .build();

        CustomException ex = assertThrows(CustomException.class, ()-> reservationService.reserveSeat(nullSeat));


        assertEquals("좌석 정보를 확인해주세요.", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getErrorCode());

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
