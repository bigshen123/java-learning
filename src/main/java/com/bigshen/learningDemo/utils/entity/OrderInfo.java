package com.bigshen.learningDemo.utils.entity;

/**
 * @ClassName OrderInfo
 * @Description:TODO
 * @Author: byj
 * @Date: 2020/5/26
 */
public class OrderInfo {
    private Integer id;
    private Integer type;
    private Integer orderId;
    private Integer creatorId;

    public OrderInfo() {
    }

    public OrderInfo(Integer id, Integer type, Integer orderId, Integer creatorId) {
        this.id = id;
        this.type = type;
        this.orderId = orderId;
        this.creatorId = creatorId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "id=" + id +
                ", type=" + type +
                ", orderId=" + orderId +
                ", creatorId=" + creatorId +
                '}';
    }
}
