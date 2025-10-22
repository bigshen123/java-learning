package com.bigshen.springbootDemo.controller;

import com.alibaba.excel.EasyExcel;
import com.bigshen.springbootDemo.ExcelRowListener;
import com.bigshen.springbootDemo.model.ImportRowDTO;
import com.bigshen.springbootDemo.service.ImportService;
import com.bigshen.springbootDemo.service.ValidationService;
import com.bigshen.springbootDemo.util.ExcelErrorWriter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/import")
@Slf4j
public class ImportController {


    private final ValidationService validationService;
    private final ImportService importService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SneakyThrows
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        InputStream is = file.getInputStream();
        ExcelRowListener listener = new ExcelRowListener(validationService);
        EasyExcel.read(is, ImportRowDTO.class, listener).sheet().doRead();

        List<ImportRowDTO> allRows = listener.getAllRows();

        List<ImportRowDTO> errors = allRows.stream()
                .filter(r -> r.getErrorInfo() != null && !r.getErrorInfo().isEmpty()).collect(Collectors.toList());
        log.info("共读取 {} 条记录，其中 {} 条有错误:{}", allRows.size(), errors.size(), errors);


        if (!errors.isEmpty()) {
            String outPath = System.getProperty("java.io.tmpdir") + "/import_errors_" + System.currentTimeMillis() + ".xlsx";
            ExcelErrorWriter.writeErrorFile(allRows, outPath);

            byte[] bytes = Files.readAllBytes(Paths.get(outPath));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=import_errors.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(bytes);
        } else {
            importService.batchInsert(allRows);
            return ResponseEntity.ok("导入完成，已写入数据库，共 " + allRows.size() + " 条");
        }
    }
}
