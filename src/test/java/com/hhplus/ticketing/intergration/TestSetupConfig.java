package com.hhplus.ticketing.intergration;

import com.hhplus.ticketing.domain.concert.entity.Concert;
import com.hhplus.ticketing.domain.concert.entity.ConcertDetail;
import com.hhplus.ticketing.domain.concert.entity.ConcertSeat;
import com.hhplus.ticketing.domain.reservation.entity.Reservation;
import com.hhplus.ticketing.infrastructure.concert.ConcertDetailJpaRepository;
import com.hhplus.ticketing.infrastructure.concert.ConcertJpaRepository;
import com.hhplus.ticketing.infrastructure.concert.ConcertSeatJpaRepository;
import com.hhplus.ticketing.infrastructure.reservation.ReservationJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.List;

@TestConfiguration
public class TestSetupConfig {

    @Autowired
    private ConcertJpaRepository concertJpaRepository;
    @Autowired
    private ConcertDetailJpaRepository concertDetailJpaRepository;
    @Autowired
    private ConcertSeatJpaRepository concertSeatJpaRepository;
    @Autowired
    private ReservationJpaRepository reservationJpaRepository;

    @Bean
    public ConcertSeat seat01() {
        ConcertSeat seat = ConcertSeat.builder()
                .seatId(1L)
                .seatNo("A01")
                .seatPrice(10000)
                .status(ConcertSeat.Status.AVAILABLE)
                .build();
        concertSeatJpaRepository.save(seat);
        return seat;
    }

    @Bean
    public ConcertSeat seat02() {
        ConcertSeat seat = ConcertSeat.builder()
                .seatId(10L)
                .seatNo("B01")
                .seatPrice(5000)
                .status(ConcertSeat.Status.OCCUPIED)
                .build();
        concertSeatJpaRepository.save(seat);
        return seat;
    }

    @Bean
    public ConcertDetail concertDetail(ConcertSeat seat01, ConcertSeat seat02) {
        ConcertDetail detail = ConcertDetail.builder()
                .detailId(1L)
                .concertDate(LocalDateTime.of(2024, 10, 1, 18, 0, 0))
                .concertSeat(List.of(seat01, seat02))
                .build();
        concertDetailJpaRepository.save(detail);
        return detail;
    }

    @Bean
    public Concert concert(ConcertDetail concertDetail) {
        Concert concert = Concert.builder()
                .concertTitle("콘서트01")
                .consertDetail(List.of(concertDetail))
                .build();
        concertJpaRepository.save(concert);
        return concert;
    }

    @Bean
    public Reservation reservation(ConcertSeat seat01) {
        Reservation reservation = Reservation.builder()
                .reservationId(1L)
                .userId(1L)
                .concertSeat(seat01)
                .concertTitle("Concert")
                .reservationDate(LocalDateTime.now())
                .status(Reservation.Status.WAITING)
                .totalPrice(10000)
                .build();
        reservationJpaRepository.save(reservation);
        return reservation;
    }

}
