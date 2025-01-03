package com.bigshen.learningDemo.javaSE.lambda.time;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * @author byj
 * @date 2022/11/15
 */
public class LocalDateTimeDemo {

    @Test
    public void localDateTimeDemo1(){
        Date date = new Date();
        Date from = Date.from(LocalDateTime.now().plusMinutes(3).atZone(ZoneId.systemDefault()).toInstant());
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        LocalDateTime localDateTime = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().plusMinutes(3);
        System.out.println(localDateTime);
        System.out.println(now.isAfter(localDateTime));
    }
}
