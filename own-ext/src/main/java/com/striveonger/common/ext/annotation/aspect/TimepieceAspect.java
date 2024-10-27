package com.striveonger.common.ext.annotation.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Mr.Lee
 * @since 2024-10-27 12:21
 */
@Aspect
@Component
public class TimepieceAspect {
    private final Logger log = LoggerFactory.getLogger(TimepieceAspect.class);

    @Around("@annotation(com.striveonger.common.ext.annotation.Timepiece)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = point.proceed();
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        log.info("Execute {}.{} duration is {} milliseconds.", point.getSignature().getDeclaringTypeName(), point.getSignature().getName(), elapsedTime);
        return result;
    }
}
