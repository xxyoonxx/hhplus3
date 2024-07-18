package com.hhplus.ticketing.presentation.interceptor;

import com.hhplus.ticketing.application.userQueue.service.UserQueueProcessService;
import com.hhplus.ticketing.common.exception.CustomException;
import com.hhplus.ticketing.domain.userQueue.UserQueueErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Component
public class UserQueueInterceptor implements HandlerInterceptor {

    private final UserQueueProcessService userQueueProcessService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) throw new CustomException(UserQueueErrorCode.QUEUE_NOT_FOUND);
        userQueueProcessService.validateToken(token);
        return true;
    }

}
