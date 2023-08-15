package com.bigshen.learningDemo.design.strategy.driver;

import com.bigshen.learningDemo.utils.EnumUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author byj
 * @date 2023/8/15
 */
public enum StrategyType {

    ONE("one"),

    TWO("two"),

    THREE("three");
    private final String type;

    StrategyType(String type){
       this.type = type;
    }

    @JsonValue
    public String getEnumName() {
        return this.type;
    }

    @JsonCreator
    public static StrategyType valueOfWithFormat(String type) {
        return EnumUtil.valueOfWithFormat(StrategyType.class, type);
    }

    public static StrategyType valueOfWithFormat(String type, StrategyType defaultValue) {
        return EnumUtil.valueOfWithFormat(StrategyType.class, type, defaultValue);
    }

}
