package com.hhplus.ticketing.common.config;

import com.hhplus.ticketing.presentation.interceptor.UserQueueInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private UserQueueInterceptor userQueueInterceptor;

    @Autowired
    public WebConfig(UserQueueInterceptor userQueueInterceptor) {
        this.userQueueInterceptor = userQueueInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userQueueInterceptor)
                .addPathPatterns("/concert","/reservation");
    }

}
