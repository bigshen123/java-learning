package com.bigshen.learningDemo.javaSE.easyexcel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.util.Date;

/**
 * @author byj
 * @date 2024/11/29
 * @Description
 */
@Data
@ContentRowHeight()
public class DemoData {

    @ExcelProperty("字符串标题")
    @ColumnWidth(20)
    private String string;

    @ExcelProperty("日期标题")
    @ColumnWidth(30)
    private Date date;

    @ColumnWidth(10)
    @ExcelProperty("数字标题")
    private Double doubleData;
    /**
     * 忽略这个字段
     */
    @ExcelIgnore
    private String ignore;
}