package com.bigshen.springbootDemo.util;

import com.alibaba.excel.EasyExcel;
import com.bigshen.springbootDemo.model.ImportRowDTO;

import java.io.File;
import java.util.List;

public class ExcelErrorWriter {
    public static File writeErrorFile(List<ImportRowDTO> rows, String outPath) {
        File file = new File(outPath);
        EasyExcel.write(file, ImportRowDTO.class)
                .sheet("错误数据")
                .doWrite(rows);
        return file;
    }
}
