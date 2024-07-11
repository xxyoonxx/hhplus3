package com.hhplus.ticketing.service;

import com.hhplus.ticketing.domain.concert.entity.Concert;
import com.hhplus.ticketing.domain.concert.entity.ConcertDetail;
import com.hhplus.ticketing.domain.concert.entity.ConcertSeat;
import com.hhplus.ticketing.domain.concert.repository.ConcertDetailRepository;
import com.hhplus.ticketing.domain.concert.repository.ConcertRepository;
import com.hhplus.ticketing.domain.concert.repository.ConcertSeatRepository;
import com.hhplus.ticketing.domain.concert.service.ConcertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConcertServiceTest {

    @Mock
    private ConcertRepository concertRepository;

    @Mock
    private ConcertSeatRepository concertSeatRepository;

    @Mock
    private ConcertDetailRepository concertDetailRepository;

    @InjectMocks
    private ConcertService concertService;

    private ConcertSeat seat01;
    private ConcertSeat seat02;
    private ConcertDetail concertDetail;
    private Concert concert;

    @BeforeEach
    void setUp() {
        seat01 = ConcertSeat.builder()
                .seatId(1L)
                .seatNo("A01")
                .seatPrice(10000)
                .status(ConcertSeat.Status.AVAILABLE)
                .build();
        seat02 = ConcertSeat.builder()
                .seatId(10L)
                .seatNo("B01")
                .seatPrice(5000)
                .status(ConcertSeat.Status.OCCUPIED)
                .build();
        concertDetail = ConcertDetail.builder()
                .detailId(1L)
                .concertDate(LocalDateTime.of(2024, 10, 1, 18, 0, 0))
                .concertSeat(List.of(seat01, seat02))
                .build();
        concert = Concert.builder()
                .title("콘서트01")
                .consertDetail(List.of(concertDetail))
                .build();
    }

    @Test
    @DisplayName("콘서트 목록 조회")
    void getConcerts() {
        when(concertRepository.getAllConcerts()).thenReturn(List.of(concert));

        List<Concert> concertList = concertService.getAllConcerts();

        assertThat(concertList).hasSize(1);
        assertThat(concertList.get(0).getTitle()).isEqualTo("콘서트01");
        assertThat(concertList.get(0).getConsertDetail().get(0).getConcertSeat()).hasSize(2);
    }

    @Test
    @DisplayName("특정 콘서트 날짜 조회")
    void getConcertDate() {
        Long concertId = concert.getConcertId();

        when(concertDetailRepository.getConcertDetailInfo(concertId)).thenReturn(List.of(concertDetail));

        List<ConcertDetail> details = concertService.getConcertDetails(concertId);

        assertThat(details).hasSize(1);
        assertThat(details.get(0).getConcertDate()).isEqualTo(concertDetail.getConcertDate());

    }

    @Test
    @DisplayName("특정 날짜 좌석 조회")
    void getConcertSeats() {
        Long detailId = concertDetail.getDetailId();

        when(concertSeatRepository.getConcertSeatsInfo(detailId)).thenReturn(List.of(seat01, seat02));

        List<ConcertSeat> seats = concertService.getConcertSeats(detailId);

        assertThat(seats).hasSize(2);
        assertThat(seats.get(0).getSeatNo()).isEqualTo("A01");
        assertThat(seats.get(1).getSeatNo()).isEqualTo("B01");
    }

}
