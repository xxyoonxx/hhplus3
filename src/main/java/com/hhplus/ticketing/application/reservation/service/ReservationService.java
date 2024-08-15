package com.hhplus.ticketing.application.reservation.service;

import com.hhplus.ticketing.common.exception.CustomException;
import com.hhplus.ticketing.domain.concert.entity.ConcertSeat;
import com.hhplus.ticketing.domain.concert.repository.ConcertDetailRepository;
import com.hhplus.ticketing.domain.concert.repository.ConcertRepository;
import com.hhplus.ticketing.domain.concert.repository.ConcertSeatRepository;
import com.hhplus.ticketing.domain.reservation.event.ReservationEvent;
import com.hhplus.ticketing.domain.payment.entity.Payment;
import com.hhplus.ticketing.domain.payment.repository.PaymentRepository;
import com.hhplus.ticketing.domain.reservation.ReservationErrorCode;
import com.hhplus.ticketing.domain.reservation.entity.Reservation;
import com.hhplus.ticketing.domain.reservation.repository.ReservationRepository;
import com.hhplus.ticketing.presentation.reservation.dto.ReservationRequestDto;
import com.hhplus.ticketing.common.redis.RedissonLock;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ConcertSeatRepository concertSeatRepository;
    private final ConcertRepository concertRepository;
    private final ConcertDetailRepository concertDetailRepository;
    private final PaymentRepository paymentRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 좌석 예약
     * @param requestDto
     * @return
     */
    @Transactional
    @RedissonLock(value = "'reserveLock:'.concat(#requestDto.getSeatId())")
    public Reservation reserveSeat(ReservationRequestDto requestDto) {

        ConcertSeat concertSeat = concertSeatRepository.getConcertSeatInfo(requestDto.getSeatId())
                .orElseThrow(() -> new CustomException(ReservationErrorCode.NO_SEAT_FOUND));
        if( concertSeat.getStatus()==ConcertSeat.Status.OCCUPIED) throw new CustomException(ReservationErrorCode.NO_SEAT_AVAILABLE);

        // 콘서트 정보 가져오기
        long concertId = concertDetailRepository.getConcertInfoByDetailId(requestDto.getDetailId()).getConcert().getConcertId();
        String concertTitle = concertRepository.getConcertInfo(concertId).getConcertTitle();

        // 좌석 배정
        concertSeat.seatOccupied();

        // 예약생성
        Reservation reservation = Reservation.of(requestDto.getUserId(), concertSeat, concertTitle, LocalDateTime.now()
                , Reservation.Status.WAITING, concertSeat.getSeatPrice());
        reservationRepository.save(reservation);

        // 결제생성
        Payment payment = Payment.of(reservation, requestDto.getTotalPrice(), Payment.Status.WAITING);
        paymentRepository.save(payment);

        // 데이터 전송 플랫폼 이벤트 발행
        applicationEventPublisher.publishEvent(new ReservationEvent(this, reservation.getReservationId()));

        return reservation;

    }

}
