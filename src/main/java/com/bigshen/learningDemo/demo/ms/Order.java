package com.bigshen.learningDemo.demo.ms;

import lombok.*;

import java.math.BigDecimal;

/**
 * @Author BYJ
 * @Date 2023/3/12 10:17
 * @Describe
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {
    private long id;
    private String shop;
    BigDecimal value;
}
