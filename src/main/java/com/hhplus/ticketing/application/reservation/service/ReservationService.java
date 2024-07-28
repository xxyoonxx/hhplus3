package com.hhplus.ticketing.application.reservation.service;

import com.hhplus.ticketing.common.exception.CustomException;
import com.hhplus.ticketing.domain.concert.entity.ConcertSeat;
import com.hhplus.ticketing.domain.concert.repository.ConcertDetailRepository;
import com.hhplus.ticketing.domain.concert.repository.ConcertRepository;
import com.hhplus.ticketing.domain.concert.repository.ConcertSeatRepository;
import com.hhplus.ticketing.domain.payment.entity.Payment;
import com.hhplus.ticketing.domain.payment.repository.PaymentRepository;
import com.hhplus.ticketing.domain.reservation.ReservationErrorCode;
import com.hhplus.ticketing.domain.reservation.entity.Reservation;
import com.hhplus.ticketing.domain.reservation.repository.ReservationRepository;
import com.hhplus.ticketing.presentation.reservation.dto.ReservationRequestDto;
import com.hhplus.ticketing.redis.RedissonLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
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

    /**
     * 좌석 예약
     * @param requestDto
     * @return
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @RedissonLock(value = "'reserveLock:'.concat(#requestDto.getSeatId())")
    public Reservation reserveSeat(ReservationRequestDto requestDto) {

        ConcertSeat concertSeat = concertSeatRepository.getConcertSeatInfo(requestDto.getSeatId())
                .orElseThrow(() -> new CustomException(ReservationErrorCode.NO_SEAT_FOUND));
        if( concertSeat.getStatus()==ConcertSeat.Status.OCCUPIED) throw new CustomException(ReservationErrorCode.NO_SEAT_AVAILABLE);

        // 콘서트 정보 가져오기
        long concertId = concertDetailRepository.getConcertInfoByDetailId(requestDto.getDetailId()).getConcert().getConcertId();
        String concertTitle = concertRepository.getConcertInfo(concertId).getConcertTitle();

        // 좌석 배정
        concertSeat.changeStatus(ConcertSeat.Status.OCCUPIED);

        // 예약생성
        Reservation reservation = Reservation.builder()
                .userId(requestDto.getUserId())
                .concertSeat(concertSeat)
                .concertTitle(concertTitle)
                .reservationDate(LocalDateTime.now())
                .status(Reservation.Status.WAITING)
                .totalPrice(concertSeat.getSeatPrice())
                .build();
        reservationRepository.save(reservation);

        // 결제생성
        Payment payment = Payment.builder()
                .payAmount(requestDto.getTotalPrice())
                .status(Payment.Status.WAITING)
                .payDate(LocalDateTime.now())
                .reservation(reservation)
                .build();
        paymentRepository.save(payment);

        return reservation;

    }

}
