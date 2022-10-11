package com.bigshen.learningDemo.common.model;

/**
 * @author byj
 * @date 2022/10/11
 */
public interface Resource extends Model {

    void setId(String id);

    String getId();


    /**
     * 以下接口用于数据校验分组
     */
    interface Save {
    }

    interface Update {
    }
}
