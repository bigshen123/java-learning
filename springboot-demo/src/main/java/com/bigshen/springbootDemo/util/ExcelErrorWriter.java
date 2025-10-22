package com.bigshen.springbootDemo.util;

import com.alibaba.excel.EasyExcel;
import com.bigshen.springbootDemo.model.ImportRowDTO;

import java.io.File;
import java.util.List;

public class ExcelErrorWriter {

    public static File writeErrorFile(List<ImportRowDTO> rows, String outPath) {
        File outFile = new File(outPath);
        outFile.getParentFile().mkdirs();
        EasyExcel.write(outFile, ImportRowDTO.class)
                .sheet("errors")
                .doWrite(rows);
        return outFile;
    }
}
