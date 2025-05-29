package com.bigshen.springbootDemo.config.mdc;

import lombok.Getter;
import lombok.Setter;

/**
 * @author byj
 * @date 2025/5/29
 * @Description
 */
@Getter
@Setter
public class TraceContext {
    private String traceId;
    private String uri;
    private long startTime;
    private long dbStartTime;
    private long dbCostTime;
    private long totalCostTime;
    private String status;
    private String errorMsg;

    public void markStart() {
        this.startTime = System.currentTimeMillis();
    }

    public void markDbStart() {
        this.dbStartTime = System.currentTimeMillis();
    }

    public void markDbEnd() {
        this.dbCostTime += System.currentTimeMillis() - this.dbStartTime;
    }

    public long totalCost() {
        return System.currentTimeMillis() - this.startTime;
    }
}
