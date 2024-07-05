package com.hhplus.ticketing.presentation.reservation;

import com.hhplus.ticketing.presentation.reservation.dto.ReservationResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    /**
     * 좌석 에약 요청
     * @param request
     * @return
     */
    @PostMapping("/")
    public ReservationResponseDto reserve(HttpServletRequest request){
        return new ReservationResponseDto(1,"DONE");
    }

}
