package com.bigshen.learningDemo.design.strategy.share;

import java.util.HashMap;
import java.util.Map;

/**
 * @author byj
 * @date 2022/2/9
 */
public class ShareFactory {
    // 定义策略枚举
    enum ShareType {
        SINGLE("single", "单商品"),
        MULTI("multi", "多商品"),
        ORDER("order", "下单");
        // 场景对应的编码
        private String code;

        // 业务场景描述
        private String desc;
        ShareType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public String getCode() {
            return code;
        }
        // 省略 get set 方法
    }
    // 定义策略map缓存
    private static final Map<String, ShareStrategy> shareStrategies = new HashMap<>();
    static {
        shareStrategies.put("order", new OrderItemShare());
        shareStrategies.put("single", new SingleItemShare());
        shareStrategies.put("multi", new MultiItemShare());
    }
    // 获取指定策略
    public static ShareStrategy getShareStrategy(String type) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("type should not be empty.");
        }
        return shareStrategies.get(type);
    }

    public static void main(String[] args) {
        // 测试demo
        String shareType = "order";
        ShareStrategy shareStrategy = ShareFactory.getShareStrategy(shareType);
        shareStrategy.shareAlgorithm("order");
        // 输出结果：当前分享图片是order
    }
}
