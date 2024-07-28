package com.hhplus.ticketing.domain.reservation.entity;

import com.hhplus.ticketing.domain.concert.entity.ConcertSeat;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name="reservation")
public class Reservation {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
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

    @Version
    private int version;

    public enum Status {
        WAITING, DONE, EXPIRED
    }

    @Builder
    public Reservation(Long reservationId, long userId, ConcertSeat concertSeat, String concertTitle, LocalDateTime reservationDate
            , Status status, int totalPrice, int version) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.concertSeat = concertSeat;
        this.concertTitle = concertTitle;
        this.reservationDate = reservationDate;
        this.status = status;
        this.totalPrice = totalPrice;
        this.version = version;
    }

    // 예약 상태값 변경
    public Reservation changeStatus(Status status) {
        this.status = status;
        return this;
    }

}
