package com.bigshen.springbootDemo.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ImportRowDTO {
    @ExcelProperty("id")
    private String id;

    @ExcelProperty("name")
    private String name;

    @ExcelProperty("age")
    private String age;

    @ExcelProperty("phone")
    private String phone;

    @ExcelProperty("email")
    private String email;

    // error column for write-back
    private String errorInfo;
}
