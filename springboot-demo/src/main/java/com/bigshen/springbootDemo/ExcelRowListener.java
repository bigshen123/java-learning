package com.bigshen.springbootDemo;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.bigshen.springbootDemo.model.ImportRowDTO;
import com.bigshen.springbootDemo.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ExcelRowListener extends AnalysisEventListener<ImportRowDTO> {

    private final ValidationService validationService;
    private final List<ImportRowDTO> buffer = new ArrayList<>();
    private final int batchSize = 500;
    private final List<ImportRowDTO> allRows = new ArrayList<>();

    @Override
    public void invoke(ImportRowDTO data, AnalysisContext context) {
        buffer.add(data);
        if (buffer.size() >= batchSize) {
            flushBuffer();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (!buffer.isEmpty()) {
            flushBuffer();
        }
    }

    private void flushBuffer() {
        validationService.validateAll(new ArrayList<>(buffer));
        allRows.addAll(buffer);
        buffer.clear();
    }

    public List<ImportRowDTO> getAllRows() {
        return allRows;
    }
}
