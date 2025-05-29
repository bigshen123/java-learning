package com.bigshen.springbootDemo.config.mdc;

import com.bigshen.springbootDemo.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author byj
 * @date 2025/5/29
 * @Description
 */
@Component
@Slf4j
public class TraceInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        TraceContext context = new TraceContext();
        context.setTraceId(UUID.randomUUID().toString().replace("-", ""));
        context.setUri(request.getRequestURI());
        context.markStart();
        MDC.put("traceId", context.getTraceId());
        MDC.put("trace", JsonUtils.toJson(context));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String traceJson = MDC.get("trace");
        if (traceJson != null) {
            TraceContext context = JsonUtils.fromJson(traceJson, TraceContext.class);
            context.setTotalCostTime(System.currentTimeMillis() - context.getStartTime());
            context.setStatus(ex == null ? "SUCCESS" : "FAILURE");
            if (ex != null) {
                context.setErrorMsg(ex.getMessage());
            }
            log.info("接口链路日志：trace={}", JsonUtils.toJson(context));
        }
        MDC.clear();
    }
}
