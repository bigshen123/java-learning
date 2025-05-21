package com.bigshen.springcloudconsuldemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author byj
 * @date 2025/5/21
 * @Description
 */
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "hello,world!";
    }
}
