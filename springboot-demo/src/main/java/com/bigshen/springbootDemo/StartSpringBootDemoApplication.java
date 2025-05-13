package com.bigshen.springbootDemo;

import com.bigshen.springbootDemo.service.impl.UserTerminalService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author byj
 * @date 2023/12/8
 * @Description
 */
@SpringBootApplication
@EnableAspectJAutoProxy
public class StartSpringBootDemoApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(StartSpringBootDemoApplication.class, args);
//        UserTerminalService service = context.getBean(UserTerminalService.class);
//
//        // 模拟用户登录
//        service.saveUserTerminal("group1", "user123", "terminalA"); // 第一次存入
//        service.saveUserTerminal("group1", "user123", "terminalB"); // 新增终端
//        service.saveUserTerminal("group1", "user123", "terminalA"); // 终端已存在，跳过
//        service.saveUserTerminal("group2", "user123", "terminalC");
//        service.saveUserTerminal("group1", "user456", "terminalC");
    }
}