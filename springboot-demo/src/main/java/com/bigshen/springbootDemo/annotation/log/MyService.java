package com.bigshen.springbootDemo.annotation.log;

import org.springframework.stereotype.Service;

/**
 * @author byj
 * @date 2025/4/16
 * @Description
 */
@Service
public class MyService {
    @LogExecution("测试方法")
    public String testMethod() throws InterruptedException {
        Thread.sleep(300);
        return "执行完成";
    }
}
