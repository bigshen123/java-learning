package com.bigshen.learningDemo.demo;


import com.bigshen.learningDemo.common.constants.TableType;

/**
 * @author byj
 * @date 2022/10/24
 */
public class EnumTest {
    public static void main(String[] args) {
        for (TableType value : TableType.values()) {
            System.out.println(value.name());
        }
    }
}
