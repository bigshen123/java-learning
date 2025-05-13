package com.bigshen.springbootDemo.annotation.monitor;

import org.springframework.stereotype.Service;

/**
 * @author byj
 * @date 2025/4/16
 * @Description
 */
@Service
public class MonitorService {

    @PerfMonitor(threshold = 500, level = "ERROR")
    public String performTask() {
        // 模拟一个需要时间的操作
        try {
            Thread.sleep(600);  // 模拟操作耗时 600ms
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Task Completed";
    }
}