package com.cat.aspect;

import com.cat.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Aspect
@Component
@Slf4j
public class ResultAspect {
    @Around("execution(public * com.cat.controller.*.*(..))")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
            Object object = proceedingJoinPoint.proceed();
            if(Objects.isNull(object) || !object.getClass().equals(Result.class)){
                object = Result.success(object);
            }
            return object;
    }
}

