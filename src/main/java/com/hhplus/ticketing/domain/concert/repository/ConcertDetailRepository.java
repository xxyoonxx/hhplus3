package com.hhplus.ticketing.domain.concert.repository;

import com.hhplus.ticketing.domain.concert.entity.ConcertDetail;

import java.util.List;

public interface ConcertDetailRepository {

    List<ConcertDetail> getConcertDetailInfo(long concertId);
    ConcertDetail getConcertInfoByDetailId(long concertDetailId);

}
