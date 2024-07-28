package com.hhplus.ticketing.domain.concert.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name="concert")
@NoArgsConstructor
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long concertId;

    private String concertTitle;

    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL)
    private List<ConcertDetail> consertDetail = new ArrayList();

    @Builder
    public Concert(long concertId, String concertTitle, List<ConcertDetail> consertDetail) {
        this.concertId = concertId;
        this.concertTitle = concertTitle;
        this.consertDetail = consertDetail;
    }

}