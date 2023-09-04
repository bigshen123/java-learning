package com.bigshen.learningDemo.design.builder.DecorationPackage;

import java.math.BigDecimal;

/**
 * @Author BYJ
 * @Date 2023/9/4 20:58
 * @Describe
 */
public interface Matter {
    /**
     * 场景；地板、地砖、涂料、吊顶
     */
    String scene();

    /**
     * 品牌
     */
    String brand();

    /**
     * 型号
     */
    String model();

    /**
     * 平米报价
     */
    BigDecimal price();

    /**
     * 描述
     */
    String desc();
}
