package com.hhplus.ticketing.presentation.reservation;

import com.hhplus.ticketing.application.reservation.service.ReservationService;
import com.hhplus.ticketing.presentation.reservation.dto.ReservationRequestDto;
import com.hhplus.ticketing.presentation.reservation.dto.ReservationResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "예약 API")
@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * 좌석 예약 요청
     * @param requestDto
     * @return
     */
    @Operation(summary = "좌석 예약 요청")
    @PostMapping("/")
    public ResponseEntity<ReservationResponseDto> reserve(@RequestBody ReservationRequestDto requestDto){
        ReservationResponseDto reservationResponseDto = ReservationResponseDto.from(reservationService.reserveSeat(requestDto));
        return ResponseEntity.ok(reservationResponseDto);
    }

}
