package com.hhplus.ticketing.domain.payment;

import com.hhplus.ticketing.common.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PaymentErrorCode implements ErrorCode {

    NOT_ENOUGH_MONEY("잔액이 부족합니다.", HttpStatus.PAYMENT_REQUIRED),
    INVALID_CHARGE_AMOUNT("0 이상의 금액만 충전 가능합니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

    PaymentErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
