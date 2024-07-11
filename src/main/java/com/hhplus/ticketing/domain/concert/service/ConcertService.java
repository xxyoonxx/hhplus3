package com.hhplus.ticketing.domain.concert.service;

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

    public List<Concert> getAllConcerts() {
        return concertRepository.getAllConcerts();
    }

    public List<ConcertDetail> getConcertDetails(long concertId) {
        return concertDetailRepository.getConcertDetailInfo(concertId);
    }

    public List<ConcertSeat> getConcertSeats(long concertDeatilId) {
        return concertSeatRepository.getConcertSeatsInfo(concertDeatilId);
    }

}
