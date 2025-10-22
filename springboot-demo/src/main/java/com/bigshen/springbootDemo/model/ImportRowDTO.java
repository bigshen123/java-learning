package com.bigshen.springbootDemo.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ImportRowDTO {
    private String id;

    @ExcelProperty(value = "姓名", index = 0)
    private String name;

    @ExcelProperty(value = "年龄", index = 1)
    private String age;

    @ExcelProperty(value = "邮箱", index = 2)
    private String email;

    @ExcelProperty(value = "手机号", index = 3)
    private String phone;

    @ExcelProperty(value = "入职日期", index = 4)
    private String date;

    // error column for write-back
    @ExcelProperty(value = "错误信息", index = 5)
    private String errorInfo;
}
