package com.pik.aop;


import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class LoggingService {
    private Logger log = Logger.getLogger(getClass());

    @After("execution(* com.pik.controller.AccountController.*(..))")
    public void log(JoinPoint point) {
        log.info(point.getSignature().getName() + " called...");
    }
}

