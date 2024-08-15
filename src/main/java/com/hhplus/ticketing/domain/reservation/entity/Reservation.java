package com.hhplus.ticketing.domain.reservation.entity;

import com.hhplus.ticketing.domain.concert.entity.ConcertSeat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    private long userId;

    @OneToOne
    @JoinColumn(name = "seat_id")
    private ConcertSeat concertSeat;

    private String concertTitle;

    private LocalDateTime reservationDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    private int totalPrice;

    public enum Status {
        WAITING, DONE, EXPIRED
    }

    public static Reservation of(long userId, ConcertSeat concertSeat, String concertTitle, LocalDateTime reservationDate, Status status, int totalPrice) {
        return new ReservationBuilder()
                .userId(userId)
                .concertSeat(concertSeat)
                .concertTitle(concertTitle)
                .reservationDate(reservationDate)
                .status(status)
                .totalPrice(totalPrice)
                .build();
    }

    // 예약 상태값 변경
    public Reservation changeStatus(Status status) {
        this.status = status;
        return this;
    }

}
