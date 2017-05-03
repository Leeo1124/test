package com.leeo.dynamicDataSource;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.leeo.datasource.DataSourceContextHolder;
import com.leeo.datasource.DataSourceType;

@Component
@Aspect
@Order(1)//关键配置，切换数据源一定要比持久层代码更先执行（事务也算持久层代码）
public class DataSourceAspect// implements MethodBeforeAdvice,AfterReturningAdvice 
{

//    @Override
//    public void afterReturning(Object returnValue, Method method,
//            Object[] args, Object target) {
//        DataSourceContextHolder.clearDataSourceType();
//    }

    @Pointcut("execution(* com.leeo..*.service.*.*(..))")
    public void serviceExecution() {
    }

    @Before("serviceExecution()")
    public void before(JoinPoint point) {
        Object target = point.getTarget();
        String meth = point.getSignature().getName();

        Class<?> clazz = target.getClass();

        Class<?>[] parameterTypes = ((MethodSignature) point.getSignature())
            .getMethod().getParameterTypes();
        Method method;
        try {
            method = clazz.getMethod(meth, parameterTypes);
            if (method.isAnnotationPresent(DataSource.class)) {
                DataSource datasource = method.getAnnotation(DataSource.class);
                System.out.println("----: " + datasource.value());
                DataSourceContextHolder.setDataSourceType(datasource.value());
            } else {
                DataSourceContextHolder
                    .setDataSourceType(DataSourceType.dataSource_plat);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

}