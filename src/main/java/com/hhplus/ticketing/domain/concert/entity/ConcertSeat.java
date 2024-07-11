package com.hhplus.ticketing.domain.concert.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="concert_seat")
@NoArgsConstructor
public class ConcertSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long seatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="detail_id")
    private ConcertDetail concertDetail;

    private String seatNo;

    private Status status;

    private int seatPrice;

    public enum Status {
        OCCUPIED,
        AVAILABLE
    }

    @Builder
    public ConcertSeat(long seatId, String seatNo, Status status, int seatPrice, ConcertDetail concertDetail) {
        this.seatId = seatId;
        this.seatNo = seatNo;
        this.status = status;
        this.seatPrice = seatPrice;
        this.concertDetail = concertDetail;
    }

}
