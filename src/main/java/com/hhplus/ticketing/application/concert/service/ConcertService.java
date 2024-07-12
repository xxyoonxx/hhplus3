package com.hhplus.ticketing.application.concert.service;

import com.hhplus.ticketing.common.exception.CustomException;
import com.hhplus.ticketing.domain.concert.entity.Concert;
import com.hhplus.ticketing.domain.concert.entity.ConcertDetail;
import com.hhplus.ticketing.domain.concert.entity.ConcertSeat;
import com.hhplus.ticketing.domain.concert.repository.ConcertDetailRepository;
import com.hhplus.ticketing.domain.concert.repository.ConcertRepository;
import com.hhplus.ticketing.domain.concert.repository.ConcertSeatRepository;
import com.hhplus.ticketing.domain.queue.QueueErrorCode;
import com.hhplus.ticketing.domain.queue.entity.Queue;
import com.hhplus.ticketing.domain.queue.repository.QueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final ConcertDetailRepository concertDetailRepository;
    private final ConcertSeatRepository concertSeatRepository;
    private final QueueRepository queueRepository;

    /**
     * 콘서트 조회
     * @return
     */
    public List<Concert> getAllConcerts(String authorization) {
        // 토큰 검증
        Queue queue = queueRepository.getUserIdByToken(authorization).orElseThrow(() -> new CustomException(QueueErrorCode.USER_NOT_FOUND));
        if (queue.getStatus() == Queue.Status.EXPIRED) throw new CustomException(QueueErrorCode.TOKEN_EXPIRED);
        return concertRepository.getAllConcerts();
    }

    /**
     * 특정 콘서트 날짜 조회
     * @param concertId
     * @return
     */
    public List<ConcertDetail> getConcertDetails(String authorization, long concertId) {
        // 토큰 검증
        Queue queue = queueRepository.getUserIdByToken(authorization).orElseThrow(() -> new CustomException(QueueErrorCode.USER_NOT_FOUND));
        if (queue.getStatus() == Queue.Status.EXPIRED) throw new CustomException(QueueErrorCode.TOKEN_EXPIRED);
        return concertDetailRepository.getConcertDetailInfo(concertId);
    }

    /**
     * 특정 날짜 좌석 조회
     * @param concertDeatilId
     * @return
     */
    public List<ConcertSeat> getConcertSeats(String authorization, long concertDeatilId) {
        // 토큰 검증
        Queue queue = queueRepository.getUserIdByToken(authorization).orElseThrow(() -> new CustomException(QueueErrorCode.USER_NOT_FOUND));
        if (queue.getStatus() == Queue.Status.EXPIRED) throw new CustomException(QueueErrorCode.TOKEN_EXPIRED);
        return concertSeatRepository.getConcertSeatsInfo(concertDeatilId);
    }

}
