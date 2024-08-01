package com.hhplus.ticketing.presentation.interceptor;

import com.hhplus.ticketing.application.userQueue.service.UserQueueServiceImpl;
import com.hhplus.ticketing.common.exception.CustomException;
import com.hhplus.ticketing.domain.userQueue.UserQueueErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class UserQueueInterceptor implements HandlerInterceptor {

    private final UserQueueServiceImpl userQueueServiceImpl;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) throw new CustomException(UserQueueErrorCode.QUEUE_NOT_FOUND);
        userQueueServiceImpl.validateToken(token);
        return true;
    }

}
