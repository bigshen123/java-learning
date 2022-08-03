package com.bigshen.learningDemo.design.strategy.driver;

import java.util.HashMap;
import java.util.Map;

/**
 * @author byj
 * @date 2022/2/9
 */
public class Context {
    /**
     缓存所有的策略，当前是无状态的，可以共享策略类对象
     */
    private static final Map<String, GearStrategy> strategies = new HashMap<>();

    // 第一种写法
    static {
        strategies.put("one", new GearStrategyOne());
        strategies.put("two", new GearStrategyTwo());
    }

    public static GearStrategy getStrategy(String type) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("type should not be empty.");
        }
        return strategies.get(type);
    }

    // 第二种写法
    public static GearStrategy getStrategySecond(String type) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("type should not be empty.");
        }
        if ("one".equals(type)) {
            return new GearStrategyOne();
        }
        if ("two".equals(type)) {
            return new GearStrategyTwo();
        }
        return null;
    }


    public static void main(String[] args) {
        // 测试结果
        GearStrategy strategyOne = Context.getStrategy("one");
        strategyOne.algorithm("1档");
        // 结果：当前档位1档
        GearStrategy strategyTwo = Context.getStrategySecond("one");
        strategyTwo.algorithm("1档");
        // 结果：当前档位1档
    }
}
