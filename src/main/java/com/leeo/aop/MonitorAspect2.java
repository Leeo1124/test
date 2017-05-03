package com.leeo.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.leeo.annotation.MonitorLogger;

// 声明这是一个组件
@Component
//声明这是一个切面Bean
@Aspect
public class MonitorAspect2 {

    private final static Logger log = LoggerFactory.getLogger(MonitorAspect2.class);

    //配置切入点,该方法无方法体,主要为方便同类中其他方法使用此处配置的切入点
    @Pointcut("@annotation(com.leeo.annotation.MonitorLogger)")
    public void aspect() {
    }

    //配置环绕通知,使用在方法aspect()上注册的切入点
    @Around("aspect() && @annotation(monitorLogger)")
    public void around(JoinPoint joinPoint, MonitorLogger monitorLogger) {
        long start = System.currentTimeMillis();
        try {
            ((ProceedingJoinPoint) joinPoint).proceed();
            long end = System.currentTimeMillis();
            log.info("around " + joinPoint + "\tUse time : " + (end - start) + " ms!");
        } catch (Throwable e) {
            long end = System.currentTimeMillis();
            log.info("around " + joinPoint + "\tUse time : " + (end - start) + " ms with exception : " + e.getMessage());
        }
    }
}