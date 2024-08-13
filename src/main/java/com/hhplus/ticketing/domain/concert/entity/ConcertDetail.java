package com.hhplus.ticketing.domain.concert.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name="concert_detail")
@NoArgsConstructor
public class ConcertDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long detailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="concert_id")
    private Concert concert;

    private LocalDateTime concertDate;

    @Builder
    public ConcertDetail(long detailId, LocalDateTime concertDate, Concert concert) {
        this.detailId = detailId;
        this.concertDate = concertDate;
        this.concert = concert;
    }

}
