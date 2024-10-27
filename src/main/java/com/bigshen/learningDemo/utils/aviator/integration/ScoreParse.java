package com.bigshen.learningDemo.utils.aviator.integration;

import com.googlecode.aviator.AviatorEvaluator;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author BYJ
 * @Date 2024/5/21 21:55
 * @Describe 积分计算转换
 * 模拟一个场景，有一个积分计算规则：
 * 普通用户：总积分=单次消费产生的积分*注册年限
 * Vip1用户：总积分=单次消费产生的积分*注册年限*10
 * Vip2用户：总积分=单次消费产生的积分*注册年限*20
 */
public class ScoreParse {
    public static BigDecimal getScoreNumbers(String type, BigDecimal score, int year) {
        Map<String, Object> paramMap = new LinkedHashMap<>(8);
        paramMap.put("score", score);
        paramMap.put("year", year);
        paramMap.put("type", type);
        AviatorEvaluator.addFunction(new Vip1Function());
        AviatorEvaluator.addFunction(new Vip2Function());
        AviatorEvaluator.addFunction(new NormalFunction());
        // vip1为Vip1Function中的getName()方法返回的值
        Object vip1 = AviatorEvaluator.execute("vip1(score,year,type)", paramMap, true);
        if (Objects.nonNull(vip1)) {
            return (BigDecimal) vip1;
        }
        // vip2为Vip2Function中的getName()方法返回的值
        Object vip2 = AviatorEvaluator.execute("vip2(score,year,type)", paramMap, true);
        if (Objects.nonNull(vip2)) {
            return (BigDecimal) vip2;
        }
        // normal为NormalFunction中的getName()方法返回的值
        Object normal = AviatorEvaluator.execute("normal(score,year,type)", paramMap, true);
        if (Objects.nonNull(normal)) {
            return (BigDecimal) normal;
        }
        return null;
    }

    public static void main(String[] args) {
        // VIP1 积分
        BigDecimal v1 = ScoreParse.getScoreNumbers("V1", new BigDecimal(100), 5);
        // VIP2 积分
        BigDecimal v2 = ScoreParse.getScoreNumbers("V2", new BigDecimal(100), 5);
        // 普通用户积分
        BigDecimal normal = ScoreParse.getScoreNumbers("normal", new BigDecimal(100), 5);

        System.out.println("VIP1 积分: " + v1);
        System.out.println("VIP2 积分: " + v2);
        System.out.println("normal 积分: " + normal);

    }
}
