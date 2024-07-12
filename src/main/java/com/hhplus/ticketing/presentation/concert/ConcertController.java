package com.hhplus.ticketing.presentation.concert;

import com.hhplus.ticketing.application.concert.service.ConcertService;
import com.hhplus.ticketing.presentation.concert.dto.ConcertDetailResponseDto;
import com.hhplus.ticketing.presentation.concert.dto.ConcertResponseDto;
import com.hhplus.ticketing.presentation.concert.dto.ConcertSeatResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
     * 콘서트 목록 조회
     * @return 콘서트 목록
     */
    @Operation(summary = "콘서트 목록 조회")
    @GetMapping("/")
    public ResponseEntity<List<ConcertResponseDto>> getConcertsList(@RequestHeader("Authorization") String authorization) {
        List<ConcertResponseDto> concertResponseDtos = ConcertResponseDto.from(concertService.getAllConcerts(authorization));
        return ResponseEntity.ok(concertResponseDtos);
    }

    /**
     * 예약 가능 날짜 조회
     * @param concertId 콘서트 ID
     * @return 예약 가능 날짜 목록
     */
    @Operation(summary = "예약 가능 날짜 조회 ")
    @GetMapping("/{concertId}/dates")
    public ResponseEntity<List<ConcertDetailResponseDto>> getConcertsByDate(@RequestHeader("Authorization") String authorization, @PathVariable long concertId) {
        List<ConcertDetailResponseDto> concertDetailResponseDtos = ConcertDetailResponseDto.from(concertService.getConcertDetails(authorization, concertId));
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
    public ResponseEntity<List<ConcertSeatResponseDto>> getAvailableSeats(@RequestHeader("Authorization") String authorization, @PathVariable long concertId, @PathVariable long detailId) {
        List<ConcertSeatResponseDto> concertSeatResponseDtos = ConcertSeatResponseDto.from(concertService.getConcertSeats(authorization, detailId));
        return ResponseEntity.ok(concertSeatResponseDtos);
    }

}
