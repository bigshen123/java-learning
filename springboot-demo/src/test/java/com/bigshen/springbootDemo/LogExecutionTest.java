package com.bigshen.springbootDemo;

import com.bigshen.springbootDemo.annotation.log.MyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

/**
 * @author byj
 * @date 2025/4/16
 * @Description
 */
@SpringBootTest
public class LogExecutionTest {

    @Autowired
    private MyService myService;

    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void testAopLog() throws Exception {
        // 可以查看spring IOC 里有那些bean
        String[] names = applicationContext.getBeanDefinitionNames();
        String result = myService.testMethod();
        System.out.println("返回结果：" + result);
    }
}