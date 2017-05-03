package com.leeo.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//声明这是一个组件
@Component
//声明这是一个切面Bean
@Aspect
public class MonitorAspect {

	private final static Logger log = LoggerFactory.getLogger(MonitorAspect.class);
	
	//配置切入点,该方法无方法体,主要为方便同类中其他方法使用此处配置的切入点
	@Pointcut("execution(* com.leeo.aop.service..*(..))")
	public void aspect(){	}
	
	/*
	 * 配置前置通知,使用在方法aspect()上注册的切入点
	 * 同时接受JoinPoint切入点对象,可以没有该参数
	 */
	@Before("aspect()")
	public void before(JoinPoint joinPoint){
		log.info("before " + joinPoint);
	}
	
	//配置后置通知,使用在方法aspect()上注册的切入点
	@After("aspect()")
	public void after(JoinPoint joinPoint){
		log.info("after " + joinPoint);
	}
	
	//配置环绕通知,使用在方法aspect()上注册的切入点
	@Around("aspect()")
	public void around(JoinPoint joinPoint){
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
	
	//配置后置返回通知,使用在方法aspect()上注册的切入点(returning好像没有起作用，返回值为null ？？)
	@AfterReturning(pointcut="aspect()", returning="result")
	public void afterReturn(JoinPoint joinPoint,Object result){
		log.info("afterReturn " + joinPoint + " return value: " + result);
		
		Signature signature = joinPoint.getSignature();  
        log.info("DeclaringType:" + signature.getDeclaringType());   
        log.info("DeclaringTypeName:" + signature.getDeclaringTypeName());  
        log.info("Modifiers:" + signature.getModifiers());  
        log.info("Method Name:" + signature.getName());  
        log.info("LongString:" + signature.toLongString());  
        log.info("ShortString:" + signature.toShortString());  
  
        for (int i = 0; i < joinPoint.getArgs().length; i++) {  
            Object arg = joinPoint.getArgs()[i];  
            if(null != arg) {  
                log.info("Args:" + arg.toString());   
            }  
        }  
  
        log.debug("Return:" + result);
	}
	
	//配置抛出异常后通知,使用在方法aspect()上注册的切入点
	@AfterThrowing(pointcut="aspect()", throwing="ex")
	public void afterThrow(JoinPoint joinPoint, Exception ex){
		log.info("afterThrow " + joinPoint + "\t" + ex.getMessage());
	}
}