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

    private String title;

    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL)
    private List<ConcertDetail> consertDetail = new ArrayList();

    @Builder
    public Concert(long concertId, String title, List<ConcertDetail> consertDetail) {
        this.concertId = concertId;
        this.title = title;
        this.consertDetail = consertDetail;
    }

}