package com.hhplus.ticketing.presentation.concert;

import com.hhplus.ticketing.presentation.concert.dto.ConcertDetailResponseDto;
import com.hhplus.ticketing.presentation.concert.dto.ConcertResponseDto;
import com.hhplus.ticketing.presentation.concert.dto.ConcertSeatResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/concerts")
@RequiredArgsConstructor
public class ConcertController {

    /**
     * 콘서트 목록 조회
     * @return
     */
    @GetMapping("/")
    public List<ConcertResponseDto> getConcertsList(){
        return List.of();
    }

    /**
     * 예약 가능 날짜 조회
     * @param concertId
     * @return
     */
    @GetMapping("/{concertId}/dates")
    public List<ConcertDetailResponseDto> getConertsByDate(@PathVariable long concertId, HttpServletRequest request) {
        return List.of();
    }

    /**
     * 예약 가능 좌석 조회
     * @param concertId
     * @param detailId
     * @return
     */
    @GetMapping("/{concertId}/dates/{detailId}/seats")
    public List<ConcertSeatResponseDto> getAvaliableSeats(@PathVariable long concertId, @PathVariable long detailId, HttpServletRequest request){
        return List.of();
    }

}
