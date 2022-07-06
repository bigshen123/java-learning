package com.bigshen.chatDemoService.design.template.lockTemplate;

/**
 * @Author BYJ
 * @Date 2022/5/6 14:54
 * @Describe
 */
public class OrderNumberCreateUtil {

    private static int num = 0;

    public String getOrderNumber() {
        return "\t 生成订单号：" + (++num);
    }
}
