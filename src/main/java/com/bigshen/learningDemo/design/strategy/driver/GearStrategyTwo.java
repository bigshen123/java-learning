package com.bigshen.learningDemo.design.strategy.driver;

/**
 * @author byj
 * @date 2022/2/9
 * 一档
 */
public class GearStrategyTwo extends GearStrategyAbstract {
    @Override
    public void algorithm(String param) {
        System.out.println("当前档位" + param);
    }

    @Override
    boolean support(StrategyType strategyType) {
        return StrategyType.TWO.equals(strategyType);
    }
}
