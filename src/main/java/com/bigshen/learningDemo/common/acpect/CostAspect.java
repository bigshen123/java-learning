package com.bigshen.learningDemo.common.acpect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;


/**
 * 所有的前端controller层的拦截业务，方法的执行时间长度，
 */
@Aspect
@Component
public class CostAspect {

    Logger logger = LoggerFactory.getLogger(CostAspect.class);

    @Pointcut("execution(* com.*.admin.controller.*.*(..))")
    private void pointCutMethodController() {

    }

    @Around("pointCutMethodController()")
    public Object doAroundService(ProceedingJoinPoint pjp) throws Throwable {

        long begin = System.nanoTime();

        Object obj = pjp.proceed();

        long end = System.nanoTime();

        logger.info("Controller method：{}，prams：{}，cost time：{} ns，cost：{} ms",

                pjp.getSignature().toString(), Arrays.toString(pjp.getArgs()), (end - begin), (end - begin) / 1000000);

        return obj;
    }

}
