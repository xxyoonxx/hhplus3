package com.hhplus.ticketing.domain.userQueue;

import com.hhplus.ticketing.common.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserQueueErrorCode implements ErrorCode {

    QUEUE_NOT_FOUND("대기 상태가 아닙니다.", HttpStatus.UNAUTHORIZED),
    NOT_IN_QUEUE("입장 순번이 아닙니다.", HttpStatus.ACCEPTED),
    TOKEN_EXPIRED("대기 시간이 만료되었습니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

    UserQueueErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
