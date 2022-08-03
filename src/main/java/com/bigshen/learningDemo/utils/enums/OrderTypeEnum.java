package com.bigshen.learningDemo.utils.enums;

/**
 * @ClassName OrderTypeEnum
 * @Description:TODO
 * @Author: byj
 * @Date: 2020/5/26
 */
public enum OrderTypeEnum {
    TYPE1(1,"类型1"),
    TYPE2(2,"类型2");

    private Integer code;
    private String name;

    OrderTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }
    public String getName() {
        return name;
    }

    public static String getNameByCode(Integer code) {
        for (OrderTypeEnum typeEnum : OrderTypeEnum.values()) {
            if (typeEnum.code.equals(code)) {
                return typeEnum.name;
            }
        }
        return "";
    }
}
