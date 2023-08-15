package com.bigshen.learningDemo.design.strategy.driver;

import com.bigshen.learningDemo.common.exception.ApiException;

import java.util.List;

/**
 * @author byj
 * @date 2022/2/9
 */
public class Context {

    private final List<? extends GearStrategyAbstract> gearStrategyAbstracts;

    public Context(List<? extends GearStrategyAbstract> gearStrategyAbstracts) {
        this.gearStrategyAbstracts = gearStrategyAbstracts;
    }

    public void getAlgorithm(String type) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("type should not be empty.");
        }
        getSupportedGearStrategy(StrategyType.valueOfWithFormat(type)).algorithm(type);
    }

    private GearStrategyAbstract getSupportedGearStrategy(StrategyType type) {
        return gearStrategyAbstracts.stream().filter(i -> i.support(type)).findFirst().orElseThrow(() -> new ApiException(500, "系统异常"));
    }
}
