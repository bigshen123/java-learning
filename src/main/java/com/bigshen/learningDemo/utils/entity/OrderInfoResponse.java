package com.bigshen.learningDemo.utils.entity;

/**
 * @ClassName OrderInfoResponse
 * @Description:TODO
 * @Author: byj
 * @Date: 2020/5/26
 */
public class OrderInfoResponse {
    private Integer id;
    private String type;
    private String typeName;
    private Integer orderId;
    private String creatorName;

    public OrderInfoResponse() {
    }

    public OrderInfoResponse(Integer id, String type, String typeName, Integer orderId, String creatorName) {
        this.id = id;
        this.type = type;
        this.typeName = typeName;
        this.orderId = orderId;
        this.creatorName = creatorName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    @Override
    public String toString() {
        return "OrderInfoResponse{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", typeName='" + typeName + '\'' +
                ", orderId=" + orderId +
                ", creatorName='" + creatorName + '\'' +
                '}';
    }
}
