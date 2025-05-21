package com.bigshen.springcloudconsuldemo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author byj
 * @date 2025/5/21
 * @Description
 */
@RefreshScope
@RestController
public class HelloController {
    /**
     * consul做配置中心
     * 注意：这里的abc是在consul中配置的key
     */
    @Value("${abc}")
    private String abc = "abc";


    @GetMapping("/hello")
    public String hello() {
        return "hello,world!";
    }


    @RequestMapping("/getLatestAbc")
    public String getLatestAbc() {
        return abc;
    }
}
