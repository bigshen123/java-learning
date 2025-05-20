package com.bigshen.springbootDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
        SpringApplication.run(StartSpringBootDemoApplication.class, args);
    }
}