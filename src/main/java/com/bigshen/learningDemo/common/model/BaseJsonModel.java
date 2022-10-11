package com.bigshen.learningDemo.common.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.bigshen.learningDemo.utils.json.JacksonUtil;

/**
 * @author byj
 * @date 2022/10/11
 */
public abstract class BaseJsonModel implements Model, Cloneable {

    @ExcelIgnore
    private static final long serialVersionUID = 7770611713300789789L;

    protected String toJsonString() {
        return JacksonUtil.toJsonString(this);
    }

    @Override
    public String toString() {
        return toJsonString();
    }

    @Override
    public Object clone() {
        return JacksonUtil.parseObject(this.toJsonString(), this.getClass());
    }

}
