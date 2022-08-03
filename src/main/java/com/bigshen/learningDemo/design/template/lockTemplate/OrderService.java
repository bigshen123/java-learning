package com.bigshen.learningDemo.design.template.lockTemplate;

/**
 * @Author BYJ
 * @Date 2022/5/6 14:53
 * @Describe
 */
public class OrderService {
    private OrderNumberCreateUtil orderNumberCreateUtil = new OrderNumberCreateUtil();

    public void getOrderNumber() {
        ZkLock zkLock = new ZkDistributedLock();
        zkLock.zkLock();
        try {

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            zkLock.zkUnlock();
        }
        System.out.println(orderNumberCreateUtil.getOrderNumber());
    }
}
