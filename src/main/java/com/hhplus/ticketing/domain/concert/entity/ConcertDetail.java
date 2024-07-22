package com.hhplus.ticketing.domain.concert.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="concert_detail")
@NoArgsConstructor
public class ConcertDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long detailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="concert_id")
    private Concert concert;

    private LocalDateTime concertDate;

    @OneToMany(mappedBy = "concertDetail", cascade = CascadeType.ALL)
    private List<ConcertSeat> concertSeat = new ArrayList<>();

    @Builder
    public ConcertDetail(long detailId, LocalDateTime concertDate, List<ConcertSeat> concertSeat, Concert concert) {
        this.detailId = detailId;
        this.concertDate = concertDate;
        this.concertSeat = concertSeat;
        this.concert = concert;
    }

}
