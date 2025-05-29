package com.bigshen.springbootDemo.config.mdc;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

/**
 * @author byj
 * @date 2025/5/29
 * @Description
 */
public class MdcTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        // 复制调用方（主线程）上下文
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            try {
                if (contextMap != null) {
                    MDC.setContextMap(contextMap);
                }
                runnable.run();
            } finally {
                // 仅清理当前（异步）线程的 MDC，不影响主线程
                MDC.clear();
            }
        };
    }
}
