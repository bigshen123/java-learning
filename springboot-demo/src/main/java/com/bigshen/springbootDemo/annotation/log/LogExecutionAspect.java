package com.bigshen.springbootDemo.annotation.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author byj
 * @date 2025/4/16
 * @Description
 */
@Aspect
@Component
public class LogExecutionAspect {
    @Around("@annotation(com.bigshen.springbootDemo.annotation.log.LogExecution)")
    public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogExecution logExecution = method.getAnnotation(LogExecution.class);

        String methodName = method.getName();
        System.out.println("🔔 [AOP] 开始执行方法: " + methodName);
        if (!logExecution.value().isEmpty()) {
            System.out.println("👉 注解说明: " + logExecution.value());
        }

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();  // 执行目标方法
        long endTime = System.currentTimeMillis();

        System.out.println("✅ [AOP] 方法执行完成: " + methodName + "，耗时: " + (endTime - startTime) + "ms");
        return result;
    }
}
