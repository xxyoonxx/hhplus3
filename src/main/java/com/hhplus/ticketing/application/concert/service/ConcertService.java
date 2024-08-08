package com.hhplus.ticketing.application.concert.service;

import com.hhplus.ticketing.common.exception.CustomException;
import com.hhplus.ticketing.domain.concert.ConcertErrorCode;
import com.hhplus.ticketing.domain.concert.entity.Concert;
import com.hhplus.ticketing.domain.concert.entity.ConcertDetail;
import com.hhplus.ticketing.domain.concert.entity.ConcertSeat;
import com.hhplus.ticketing.domain.concert.repository.ConcertDetailRepository;
import com.hhplus.ticketing.domain.concert.repository.ConcertRepository;
import com.hhplus.ticketing.domain.concert.repository.ConcertSeatRepository;
import com.hhplus.ticketing.presentation.concert.dto.ConcertDetailResponseDto;
import com.hhplus.ticketing.presentation.concert.dto.ConcertResponseDto;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final ConcertDetailRepository concertDetailRepository;
    private final ConcertSeatRepository concertSeatRepository;

    /**
     * 콘서트 생성
     * @param concert
     * @return
     */
    @CacheEvict(value = {"concerts", "concertDetail"}, allEntries = true)
    public Concert addConcert(Concert concert) {
        return concertRepository.save(concert);
    }

    /**
     * 콘서트 조회
     * @return
     */
    @Cacheable(value="concerts")
    public Page<ConcertResponseDto> getAllConcerts(Pageable pageable) {
        Page<ConcertResponseDto> concerts = concertRepository.getAllConcerts(pageable).map(ConcertResponseDto::from);
        if (concerts.isEmpty() || concerts.stream().toList().size()==0) {
            throw new CustomException(ConcertErrorCode.NO_CONCERT_AVALIABLE);
        }
        return concerts;
    }

    /**
     * 특정 콘서트 날짜 조회
     * @param concertId
     * @return
     */
    @Cacheable(value="concertDetail", key="#concertId")
    public List<ConcertDetailResponseDto> getConcertDetails(long concertId) {
        List<ConcertDetail> concertDetails = concertDetailRepository.getConcertDetailInfo(concertId);
        if (concertDetails.isEmpty() || concertDetails.size()==0) {
            throw new CustomException(ConcertErrorCode.NO_DATE_AVALIABLE);
        }
        return ConcertDetailResponseDto.from(concertDetails);
    }

    /**
     * 특정 날짜 좌석 조회
     * @param concertDeatilId
     * @return
     */
    public List<ConcertSeat> getConcertSeats(long concertDeatilId) {
        List<ConcertSeat> concertSeats = concertSeatRepository.getConcertSeatsInfo(concertDeatilId);
        if (concertSeats.isEmpty() || concertSeats.size()==0) {
            throw new CustomException(ConcertErrorCode.NO_SEAT_AVALIABLE);
        }
        return concertSeats;
    }

}