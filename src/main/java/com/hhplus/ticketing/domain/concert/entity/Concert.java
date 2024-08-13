package com.hhplus.ticketing.domain.concert.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name="concert")
@NoArgsConstructor
public class Concert implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long concertId;

    private String concertTitle;


    @Builder
    public Concert(long concertId, String concertTitle) {
        this.concertId = concertId;
        this.concertTitle = concertTitle;
    }

}