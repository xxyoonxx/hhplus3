package com.hhplus.ticketing.intergration;

import com.hhplus.ticketing.application.concert.service.ConcertService;
import com.hhplus.ticketing.common.exception.CustomException;
import com.hhplus.ticketing.domain.concert.entity.Concert;
import com.hhplus.ticketing.domain.concert.entity.ConcertDetail;
import com.hhplus.ticketing.domain.concert.entity.ConcertSeat;
import com.hhplus.ticketing.domain.concert.repository.ConcertSeatRepository;
import com.hhplus.ticketing.domain.userQueue.entity.UserQueue;
import com.hhplus.ticketing.infrastructure.concert.ConcertDetailJpaRepository;
import com.hhplus.ticketing.infrastructure.concert.ConcertJpaRepository;
import com.hhplus.ticketing.infrastructure.userQueue.UserQueueJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Import(TestSetupConfig.class)
public class ConcertIntergrationTest {

    @Autowired
    private ConcertService concertService;

    @Autowired
    private ConcertSeat seat01;

    @Autowired
    private ConcertSeat seat02;

    @Autowired
    private ConcertDetail concertDetail;

    @Autowired
    private Concert concert;

    @Test
    @DisplayName("좌석 조회")
    void getConcertSeat() {

        List<ConcertSeat> concertSeats = concertService.getConcertSeats(1L);

        assertEquals(concertSeats.size(),2);
        assertEquals(concertSeats.get(0).getSeatNo(),"A01");

    }

    @Test
    @DisplayName("조회 불가")
    void getConcertException(){

        // 날짜 조회 실패
        CustomException ex = assertThrows(CustomException.class, ()-> concertService.getConcertDetails(0L));
        assertEquals("예약 가능한 날짜가 없습니다.", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getErrorCode());

        // 좌석 조회 실패
        ex = assertThrows(CustomException.class, ()-> concertService.getConcertSeats(0L));
        assertEquals("예약 가능한 좌석이 없습니다.", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getErrorCode());

    }


}
