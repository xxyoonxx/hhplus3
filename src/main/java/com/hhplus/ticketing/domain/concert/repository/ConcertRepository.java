package com.hhplus.ticketing.domain.concert.repository;

import com.hhplus.ticketing.domain.concert.entity.Concert;

import java.util.List;

public interface ConcertRepository {

    List<Concert> getAllConcerts();

}
