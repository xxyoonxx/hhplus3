package com.hhplus.ticketing.redis;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedissonLock {

    String value(); // 락 이름
    long waitTime() default 5000L; // 락 대기시간
    long leaseTime() default 2000L; // 락 유효시간

}
