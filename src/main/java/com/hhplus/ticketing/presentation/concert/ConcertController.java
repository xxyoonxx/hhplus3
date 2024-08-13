package com.hhplus.ticketing.presentation.concert;

import com.hhplus.ticketing.application.concert.service.ConcertService;
import com.hhplus.ticketing.domain.concert.entity.Concert;
import com.hhplus.ticketing.presentation.concert.dto.ConcertDetailResponseDto;
import com.hhplus.ticketing.presentation.concert.dto.ConcertResponseDto;
import com.hhplus.ticketing.presentation.concert.dto.ConcertSeatResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Tag(name = "콘서트 API")
@RestController
@RequestMapping("/concerts")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertService concertService;

    /**
     * 콘서트 추가
     * @param concert
     * @return
     */
    @PostMapping
    public ResponseEntity<Concert> addConcert(@RequestBody Concert concert) {
        Concert savedConcert = concertService.addConcert(concert);
        return ResponseEntity.ok(savedConcert);
    }

    /**
     * 콘서트 목록 조회
     * @return 콘서트 목록
     */
    @Operation(summary = "콘서트 목록 조회")
    @GetMapping("")
    public ResponseEntity<Page<ConcertResponseDto>> getConcertsList(@PageableDefault(size = 10) Pageable pageable) {
        Page<ConcertResponseDto> concertResponseDtos = concertService.getAllConcerts(pageable);
        return ResponseEntity.ok(concertResponseDtos);
    }

    /**
     * 예약 가능 날짜 조회
     * @param concertId 콘서트 ID
     * @return 예약 가능 날짜 목록
     */
    @Operation(summary = "예약 가능 날짜 조회 ")
    @GetMapping("/{concertId}/dates")
    public ResponseEntity<List<ConcertDetailResponseDto>> getConcertsByDate(@PathVariable long concertId) {
        List<ConcertDetailResponseDto> concertDetailResponseDtos = concertService.getConcertDetails(concertId);
        return ResponseEntity.ok(concertDetailResponseDtos);
    }

    /**
     * 예약 가능 좌석 조회
     * @param concertId 콘서트 ID
     * @param detailId 콘서트 상세 정보 ID
     * @return 예약 가능 좌석 목록
     */
    @Operation(summary = "예약 가능 좌석 조회")
    @GetMapping("/{concertId}/dates/{detailId}/seats")
    public ResponseEntity<List<ConcertSeatResponseDto>> getAvailableSeats(@PathVariable long concertId, @PathVariable long detailId) {
        List<ConcertSeatResponseDto> concertSeatResponseDtos = ConcertSeatResponseDto.from(concertService.getConcertSeats(detailId));
        return ResponseEntity.ok(concertSeatResponseDtos);
    }

}
