package com.hhplus.ticketing.domain.concert;

import com.hhplus.ticketing.common.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ConcertErrorCode implements ErrorCode {

    NO_CONCERT_AVALIABLE("예약 가능한 콘서트가 없습니다.",HttpStatus.NOT_FOUND),
    NO_DATE_AVALIABLE("예약 가능한 날짜가 없습니다.",HttpStatus.NOT_FOUND),
    NO_SEAT_AVALIABLE("예약 가능한 좌석이 없습니다.",HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus httpStatus;

    ConcertErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }


}
