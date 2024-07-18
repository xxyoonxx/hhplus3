package com.hhplus.ticketing.service;

import com.hhplus.ticketing.application.userQueue.service.UserQueueProcessService;
import com.hhplus.ticketing.common.exception.CustomException;
import com.hhplus.ticketing.domain.concert.entity.ConcertSeat;
import com.hhplus.ticketing.domain.concert.repository.ConcertSeatRepository;
import com.hhplus.ticketing.domain.payment.entity.Payment;
import com.hhplus.ticketing.domain.payment.repository.PaymentRepository;
import com.hhplus.ticketing.domain.reservation.entity.Reservation;
import com.hhplus.ticketing.domain.reservation.repository.ReservationRepository;
import com.hhplus.ticketing.domain.userQueue.UserQueueErrorCode;
import com.hhplus.ticketing.domain.userQueue.entity.UserQueue;
import com.hhplus.ticketing.domain.userQueue.repository.UserQueueRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserQueueProcessServiceTest {

    @Mock
    private UserQueueRepository userQueueRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ConcertSeatRepository concertSeatRepository;

    @InjectMocks
    private UserQueueProcessService userQueueProcessService;

    @Test
    @DisplayName("대기열 입장 검증 - NOT_IN_QUEUE 예외 발생")
    void validateWaitingToken() {
        // given
        String authorization = "validToken";
        UserQueue userQueue = UserQueue.builder()
                .status(UserQueue.Status.WAITING)
                .build();
        when(userQueueRepository.getTokenInfo(authorization)).thenReturn(Optional.ofNullable(userQueue));

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            userQueueProcessService.validateToken(authorization);
        });

        assertEquals(UserQueueErrorCode.NOT_IN_QUEUE, exception.getErrorCode());
    }

    @Test
    @DisplayName("대기열 입장 검증 - TOKEN_EXPIRED 예외 발생")
    void validateExpiredToken() {
        // given
        String authorization = "expiredToken";
        UserQueue userQueue = UserQueue.builder()
                .status(UserQueue.Status.EXPIRED)
                .build();
        when(userQueueRepository.getTokenInfo(authorization)).thenReturn(Optional.ofNullable(userQueue));

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            userQueueProcessService.validateToken(authorization);
        });

        assertEquals(UserQueueErrorCode.TOKEN_EXPIRED, exception.getErrorCode());
    }

    @Test
    @DisplayName("대기열 만료 처리 - 예약/결제 완료")
    void expireQueueSuccess() {
        // given
        long userId = 1L;
        UserQueue userQueue = UserQueue.builder()
                .userId(userId)
                .status(UserQueue.Status.PROCESSING)
                .build();
        when(userQueueRepository.getUserInfo(userId)).thenReturn(userQueue);

        ConcertSeat concertSeat = ConcertSeat.builder()
                .seatId(1L)
                .status(ConcertSeat.Status.OCCUPIED)
                .build();

        Reservation reservation = Reservation.builder()
                .reservationId(1L)
                .userId(userId)
                .concertSeat(concertSeat)
                .status(Reservation.Status.DONE)
                .build();
        when(reservationRepository.getReservationInfoByUserId(userId)).thenReturn(Optional.of(reservation));

        Payment payment = Payment.builder()
                .reservation(reservation)
                .status(Payment.Status.DONE)
                .build();
        when(paymentRepository.findByReservationId(reservation.getReservationId())).thenReturn(payment);

        // when
        userQueueProcessService.expireQueue(userId, Reservation.Status.DONE);

        // then
        assertEquals(UserQueue.Status.EXPIRED, userQueue.getStatus());
        assertEquals(Reservation.Status.DONE, reservation.getStatus());
        assertEquals(ConcertSeat.Status.OCCUPIED, concertSeat.getStatus());
        assertEquals(Payment.Status.DONE, payment.getStatus());
    }

    @Test
    @DisplayName("대기열 만료 처리 - 좌석 임시배정 만료")
    void expireQueueExpiredSeat() {
        // given
        long userId = 1L;
        UserQueue userQueue = UserQueue.builder()
                .userId(userId)
                .status(UserQueue.Status.PROCESSING)
                .build();
        when(userQueueRepository.getUserInfo(userId)).thenReturn(userQueue);

        ConcertSeat concertSeat = ConcertSeat.builder()
                .seatId(1L)
                .status(ConcertSeat.Status.OCCUPIED)
                .build();

        Reservation reservation = Reservation.builder()
                .reservationId(1L)
                .userId(userId)
                .concertSeat(concertSeat)
                .status(Reservation.Status.WAITING)
                .build();
        when(reservationRepository.getReservationInfoByUserId(userId)).thenReturn(Optional.of(reservation));
        when(concertSeatRepository.getConcertSeatInfo(concertSeat.getSeatId())).thenReturn(concertSeat);

        Payment payment = Payment.builder()
                .reservation(reservation)
                .status(Payment.Status.WAITING)
                .build();
        when(paymentRepository.findByReservationId(reservation.getReservationId())).thenReturn(payment);

        // when
        userQueueProcessService.expireQueue(userId, Reservation.Status.EXPIRED);

        // then
        assertEquals(UserQueue.Status.EXPIRED, userQueue.getStatus());
        assertEquals(Reservation.Status.EXPIRED, reservation.getStatus());
        assertEquals(ConcertSeat.Status.AVAILABLE, concertSeat.getStatus());
        assertEquals(Payment.Status.EXPIRED, payment.getStatus());
    }

    @Test
    @DisplayName("대기열 만료 처리 - 대기열 PROCESSING, 예약 안함")
    void expireQueueNoReservation() {
        // given
        long userId = 1L;
        UserQueue userQueue = UserQueue.builder()
                .userId(userId)
                .status(UserQueue.Status.PROCESSING)
                .build();
        when(userQueueRepository.getUserInfo(userId)).thenReturn(userQueue);
        when(reservationRepository.getReservationInfoByUserId(userId)).thenReturn(Optional.empty());

        // when
        userQueueProcessService.expireQueue(userId, Reservation.Status.EXPIRED);

        // then
        assertEquals(UserQueue.Status.EXPIRED, userQueue.getStatus());
        verify(reservationRepository, never()).save(any());
    }

}