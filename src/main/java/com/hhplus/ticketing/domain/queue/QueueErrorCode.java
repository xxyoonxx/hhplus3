package com.hhplus.ticketing.domain.queue;

import com.hhplus.ticketing.common.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum QueueErrorCode implements ErrorCode {

    USER_NOT_FOUND("회원 정보를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED("대기 시간이 만료되었습니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

    QueueErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
