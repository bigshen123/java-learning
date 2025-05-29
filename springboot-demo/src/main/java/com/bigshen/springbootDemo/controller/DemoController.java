package com.bigshen.springbootDemo.controller;

import com.bigshen.springbootDemo.config.mdc.TraceContext;
import com.bigshen.springbootDemo.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Executor;

/**
 * @author byj
 * @date 2025/5/29
 * @Description
 */
@RestController
@Slf4j
public class DemoController {

    @GetMapping("/test-mdc")
    public String testMdc() {
        try {
            // 模拟 DB 查询耗时
            String traceJson = MDC.get("trace");
            if (traceJson != null) {
                TraceContext context = JsonUtils.fromJson(traceJson, TraceContext.class);
                context.markDbStart();
                Thread.sleep(300); // 模拟 DB 查询
                context.markDbEnd();
                MDC.put("trace", JsonUtils.toJson(context));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "OK";
    }
}
