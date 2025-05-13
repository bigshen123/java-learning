package com.bigshen.springbootDemo;

import com.bigshen.springbootDemo.annotation.monitor.MonitorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author byj
 * @date 2025/4/16
 * @Description
 */
@SpringBootTest
public class MonitorExecutionTest {

    @Autowired
    private MonitorService monitorService;

    @Test
    public void testPerfMonitor() {
        String result = monitorService.performTask();
        System.out.println("返回结果：" + result);
    }
}
