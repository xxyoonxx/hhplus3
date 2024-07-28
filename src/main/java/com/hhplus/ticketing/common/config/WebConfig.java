package com.hhplus.ticketing.common.config;

import com.hhplus.ticketing.presentation.filter.LogFilter;
import com.hhplus.ticketing.presentation.interceptor.UserQueueInterceptor;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
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
                .addPathPatterns("/concerts/","/reservation/");
    }
    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new LogFilter());
        filterFilterRegistrationBean.setOrder(1);
        filterFilterRegistrationBean.addUrlPatterns("/*");
        return filterFilterRegistrationBean;
    }
}
