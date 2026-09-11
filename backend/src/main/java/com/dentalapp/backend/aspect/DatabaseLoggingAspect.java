package com.dentalapp.backend.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DatabaseLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(DatabaseLoggingAspect.class);

    @Pointcut("execution(* com.dentalapp.backend.model..repository..*.save(..))")
    public void repositorySaveMethods() {}

    @Before("repositorySaveMethods()")
    public void logBeforeRepositoryMethod(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        log.info("Executing database operation...{}", methodSignature);
    }
}
