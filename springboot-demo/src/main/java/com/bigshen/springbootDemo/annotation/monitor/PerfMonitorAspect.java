package com.bigshen.springbootDemo.annotation.monitor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author byj
 * @date 2025/4/16
 * @Description
 */
@Aspect
@Component
public class PerfMonitorAspect {

    private static final Logger logger = LoggerFactory.getLogger(PerfMonitorAspect.class);

    @Around("@annotation(perfMonitor)")  // 拦截所有带 @PerfMonitor 注解的方法
    public Object monitor(ProceedingJoinPoint joinPoint, PerfMonitor perfMonitor) throws Throwable {
        if (!perfMonitor.enable()) {
            // 如果禁用性能监控，直接执行
            return joinPoint.proceed();
        }

        // 记录开始时间
        long startTime = System.currentTimeMillis();

        // 执行目标方法
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Exception ex) {
            // 记录异常日志
            logger.error("Method {} threw exception: {}", joinPoint.getSignature(), ex.getMessage());
            throw ex;  // 抛出异常
        }

        // 记录结束时间
        long executionTime = System.currentTimeMillis() - startTime;

        // 判断是否超出阈值
        if (executionTime > perfMonitor.threshold()) {
            logPerformance(joinPoint, executionTime, perfMonitor);
        }

        return result;
    }

    // 根据日志级别输出不同的日志
    private void logPerformance(ProceedingJoinPoint joinPoint, long executionTime, PerfMonitor perfMonitor) {
        String methodName = joinPoint.getSignature().toShortString();
        switch (perfMonitor.level().toUpperCase()) {
            case "WARNING":
                logger.warn("Method {} took {} ms, exceeding the threshold!", methodName, executionTime);
                break;
            case "ERROR":
                logger.error("Method {} took {} ms, exceeding the threshold!", methodName, executionTime);
                break;
            default:
                logger.info("Method {} took {} ms", methodName, executionTime);
        }
    }
}
