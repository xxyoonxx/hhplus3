package com.hhplus.ticketing.application.concert.service;

import com.hhplus.ticketing.domain.concert.entity.Concert;
import com.hhplus.ticketing.domain.concert.entity.ConcertDetail;
import com.hhplus.ticketing.domain.concert.entity.ConcertSeat;
import com.hhplus.ticketing.domain.concert.repository.ConcertDetailRepository;
import com.hhplus.ticketing.domain.concert.repository.ConcertRepository;
import com.hhplus.ticketing.domain.concert.repository.ConcertSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final ConcertDetailRepository concertDetailRepository;
    private final ConcertSeatRepository concertSeatRepository;

    /**
     * 콘서트 조회
     * @return
     */
    public List<Concert> getAllConcerts(String authorization) {
        return concertRepository.getAllConcerts();
    }

    /**
     * 특정 콘서트 날짜 조회
     * @param concertId
     * @return
     */
    public List<ConcertDetail> getConcertDetails(String authorization, long concertId) {
        return concertDetailRepository.getConcertDetailInfo(concertId);
    }

    /**
     * 특정 날짜 좌석 조회
     * @param concertDeatilId
     * @return
     */
    public List<ConcertSeat> getConcertSeats(String authorization, long concertDeatilId) {
        return concertSeatRepository.getConcertSeatsInfo(concertDeatilId);
    }

}