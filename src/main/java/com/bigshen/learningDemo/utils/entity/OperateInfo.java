package com.bigshen.learningDemo.utils.entity;

/**
 * @ClassName OperatorInfo
 * @Description:TODO
 * @Author: byj
 * @Date: 2020/5/26
 */
public class OperateInfo  {
    private Integer operateId;
    private String operateName;

    public OperateInfo() {
    }

    public OperateInfo(Integer operateId, String operateName) {
        this.operateId = operateId;
        this.operateName = operateName;
    }

    public Integer getOperateId() {
        return operateId;
    }

    public void setOperateId(Integer operateId) {
        this.operateId = operateId;
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }

    @Override
    public String toString() {
        return "OperateInfo{" +
                "operateId=" + operateId +
                ", operateName='" + operateName + '\'' +
                '}';
    }
}
