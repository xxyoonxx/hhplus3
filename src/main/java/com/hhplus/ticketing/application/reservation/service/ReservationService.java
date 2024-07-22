package com.hhplus.ticketing.application.reservation.service;

import com.hhplus.ticketing.common.exception.CustomException;
import com.hhplus.ticketing.domain.concert.entity.ConcertSeat;
import com.hhplus.ticketing.domain.concert.repository.ConcertDetailRepository;
import com.hhplus.ticketing.domain.concert.repository.ConcertRepository;
import com.hhplus.ticketing.domain.concert.repository.ConcertSeatRepository;
import com.hhplus.ticketing.domain.queue.QueueErrorCode;
import com.hhplus.ticketing.domain.queue.entity.Queue;
import com.hhplus.ticketing.domain.queue.repository.QueueRepository;
import com.hhplus.ticketing.domain.reservation.ReservationErrorCode;
import com.hhplus.ticketing.domain.reservation.entity.Reservation;
import com.hhplus.ticketing.domain.reservation.repository.ReservationRepository;
import com.hhplus.ticketing.presentation.reservation.dto.ReservationRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ConcertSeatRepository concertSeatRepository;
    private final ConcertRepository concertRepository;
    private final ConcertDetailRepository concertDetailRepository;
    private final QueueRepository queueRepository;

    /**
     * 좌석 예약
     * @param requestDto
     * @param authorization
     * @return
     */
    public Reservation reserveSeat(ReservationRequestDto requestDto, String authorization) {
        ConcertSeat concertSeat = concertSeatRepository.getConcertSeatInfo(requestDto.getSeatId());
        if(concertSeat==null || concertSeat.getStatus()==ConcertSeat.Status.OCCUPIED) throw new CustomException(ReservationErrorCode.NO_SEAT_AVAILABLE);

        // 토큰 확인
        Queue queue = queueRepository.getUserIdByToken(authorization).orElseThrow(() -> new CustomException(QueueErrorCode.USER_NOT_FOUND));
        if (queue.getStatus() == Queue.Status.EXPIRED) throw new CustomException(QueueErrorCode.TOKEN_EXPIRED);
        long userId = queue.getUserId();

        // 콘서트 정보 가져오기
        long concertId = concertDetailRepository.getConcertInfoByDetailId(requestDto.getDetailId()).getConcert().getConcertId();
        String concertTitle = concertRepository.getConcertInfo(concertId).getTitle();

        // 좌석 배정
        concertSeat.setStatus(ConcertSeat.Status.OCCUPIED);
        concertSeatRepository.saveConcertSeat(concertSeat);

        Reservation reservation = Reservation.builder()
                .userId(userId)
                .concertSeat(concertSeat)
                .concertTitle(concertTitle)
                .reservationDate(LocalDateTime.now())
                .status(Reservation.Status.WAITING)
                .totalPrice(requestDto.getTotalPrice())
                .build();

        return reservationRepository.reserve(reservation);
    }

}
