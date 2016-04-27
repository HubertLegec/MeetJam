package com.pik.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class LoggingService {
    private Logger log = Logger.getLogger(getClass());

    @Around("execution(* com.pik.account..*.*(..)) || execution(* com.pik.event..*.*(..))")
    public Object log(ProceedingJoinPoint jp) throws Throwable{
        StringBuilder toLog = new StringBuilder();
        log.info(jp.getSignature().getName() + " called...");
        Object[] args=jp.getArgs();
        if(args.length>0){
            toLog.append(jp.getSignature().getName() + " called with parameters:\n\t");
            for (int i = 0; i < args.length; i++) {
                String argVal = args[i] == null ? "null" : args[i].toString();
                toLog.append("Arg" + (i+1) + ": "+ argVal + "  ");
            }
        } else{
            toLog.append(jp.getSignature().getName() + " called...");
        }
        log.info(toLog.toString());
        return jp.proceed();
    }
}

