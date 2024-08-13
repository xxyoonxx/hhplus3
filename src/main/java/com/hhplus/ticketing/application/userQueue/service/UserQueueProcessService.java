package com.hhplus.ticketing.application.userQueue.service;

import com.hhplus.ticketing.common.exception.CustomException;
import com.hhplus.ticketing.domain.concert.entity.ConcertSeat;
import com.hhplus.ticketing.domain.concert.repository.ConcertSeatRepository;
import com.hhplus.ticketing.domain.payment.entity.Payment;
import com.hhplus.ticketing.domain.payment.repository.PaymentRepository;
import com.hhplus.ticketing.domain.reservation.ReservationErrorCode;
import com.hhplus.ticketing.domain.reservation.entity.Reservation;
import com.hhplus.ticketing.domain.reservation.repository.ReservationRepository;
import com.hhplus.ticketing.domain.userQueue.entity.UserQueue;
import com.hhplus.ticketing.domain.userQueue.repository.UserQueueRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserQueueProcessService{

    private final UserQueueRepository userQueueRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final ConcertSeatRepository concertSeatRepository;

    /**
     * 대기열 만료 처리
     * @param userId
     * @param reservationStatus
     */
    @Transactional
    public void expireQueue(long userId, Reservation.Status reservationStatus) {
        UserQueue userQueue = userQueueRepository.getUserInfo(userId);
        // 대기열 EXPIRED
        userQueue.changeStatus(UserQueue.Status.EXPIRED);
        expireReservation(reservationStatus, userId);
    }

    /**
     * 대기열 만료 처리 - 예약/결제 만료 처리
     * @param reservationStatus
     * @param userId
     */
    @Transactional
    public void expireReservation(Reservation.Status reservationStatus, long userId) {
        // 예약 존재 확인
        Optional<Reservation> optionalReservation = reservationRepository.getReservationInfoByUserId(userId);
        // 진행중인 예약 존재
        optionalReservation.ifPresent(reservation -> {
            // 예약 상태 변경
            reservation.changeStatus(reservationStatus);
            reservationRepository.save(reservation);
            // 예약 EXPIRED 시 좌석 OCCPIED > AVALIABLE
            if (reservationStatus == Reservation.Status.EXPIRED) updateConcertSeatStatus(reservation);
            // 결제 상태 처리
            updatePaymentStatus(reservation, reservationStatus);
        });
    }

    /**
     * 대기열 만료 처리 - 좌석 OCCUPIED > AVAILABLE
     * @param reservation
     */
    public void updateConcertSeatStatus(Reservation reservation) {
        long seatId = reservation.getConcertSeat().getSeatId();
        ConcertSeat concertSeat = concertSeatRepository.getConcertSeatInfo(seatId)
                .orElseThrow(() -> new CustomException(ReservationErrorCode.NO_SEAT_FOUND));
        concertSeat.changeStatus(ConcertSeat.Status.AVAILABLE);
        concertSeatRepository.save(concertSeat);
    }

    /**
     * 대기열 만료 처리 - 결제 상태 변경
     * @param reservation
     * @param reservationStatus
     */
    public void updatePaymentStatus(Reservation reservation, Reservation.Status reservationStatus) {
        long reservationId = reservation.getReservationId();
        Payment payment = paymentRepository.findByReservationId(reservationId);
        payment.changeStatus(Payment.Status.EXPIRED);
        if (reservationStatus == Reservation.Status.DONE) payment.changeStatus(Payment.Status.DONE); // 예약 성공 > 결제 완료 처리
        paymentRepository.save(payment);
    }

}
