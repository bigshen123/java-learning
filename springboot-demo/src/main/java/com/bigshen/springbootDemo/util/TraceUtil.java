package com.bigshen.springbootDemo.util;

import com.bigshen.springbootDemo.config.mdc.TraceContext;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * @author byj
 * @date 2025/5/29
 * @Description
 */
public class TraceUtil {
    public static final String TRACE_KEY = "traceCtx";

    public static void init(String uri) {
        String traceId = UUID.randomUUID().toString().replace("-", "");
        TraceContext ctx = new TraceContext();
        ctx.setTraceId(traceId);
        ctx.setUri(uri);
        ctx.markStart();
        MDC.put(TRACE_KEY, JsonUtils.toJson(ctx));
        MDC.put("traceId", traceId);
    }

    public static TraceContext get() {
        String json = MDC.get(TRACE_KEY);
        return JsonUtils.fromJson(json, TraceContext.class);
    }

    public static void markDbStart() {
        get().markDbStart();
    }

    public static void markDbEnd() {
        get().markDbEnd();
        MDC.put(TRACE_KEY, JsonUtils.toJson(get())); // 更新
    }

    public static void finish(String status, String errorMsg) {
        TraceContext ctx = get();
        ctx.setStatus(status);
        ctx.setErrorMsg(errorMsg);
        MDC.put(TRACE_KEY, JsonUtils.toJson(ctx));
        MDC.put("durationMs", String.valueOf(ctx.totalCost()));
        MDC.put("dbCostMs", String.valueOf(ctx.getDbCostTime()));
        // 可持久化 ctx 至数据库
    }

    public static void clear() {
        MDC.clear();
    }
}
