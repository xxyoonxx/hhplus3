package com.hhplus.ticketing.domain.reservation;

import com.hhplus.ticketing.common.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ReservationErrorCode implements ErrorCode {

    NO_RESERVATION_INFO("예약 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NO_SEAT_FOUND("좌석 정보를 확인해주세요.", HttpStatus.NOT_FOUND),
    NO_SEAT_AVAILABLE("예약 가능한 좌석이 없습니다.",HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

    ReservationErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
