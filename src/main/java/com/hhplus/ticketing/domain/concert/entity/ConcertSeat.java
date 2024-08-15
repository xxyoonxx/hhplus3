package com.hhplus.ticketing.domain.concert.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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

    @Enumerated(EnumType.STRING)
    private Status status;

    private int seatPrice;
/*

    @Version
    private long version;
*/

    public enum Status {
        OCCUPIED,
        AVAILABLE
    }

    @Builder
    public ConcertSeat(long seatId, ConcertDetail concertDetail, String seatNo, Status status, int seatPrice) {
        this.seatId = seatId;
        this.concertDetail = concertDetail;
        this.seatNo = seatNo;
        this.status = status;
        this.seatPrice = seatPrice;
    }

    public void seatOccupied(){
        this.status = Status.OCCUPIED;
    }

    public void seatAvailable(){
        this.status = Status.AVAILABLE;
    }

}
