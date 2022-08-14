/*
 * @Copyright: 2018-2019 taojiji All rights reserved.
 */
package com.bigshen.learningDemo.utils;

import com.bigshen.learningDemo.utils.date.DateUtil01;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 导出
 */
public class ExportDemo {

	public static Integer DEFAULT_ROW_MAX_COUNT = 5000;

	public void export(HttpServletResponse response) {
		// sheet默认最大行数
		int defaultRowMaxCount = DEFAULT_ROW_MAX_COUNT;
		// 表格sheet名称
		String sheetName = "聊天内容";
		// 文件名称
		String fileName = "TEST" + "_" + sheetName + "_"
				+ DateUtil01.getDateDay() + ".xlsx";
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 创建一个sheet
		HSSFSheet sheet = workbook.createSheet();
		// 设置宽度
		sheet.setColumnWidth(0, 3000);
		sheet.setColumnWidth(1, 3000);
		sheet.setColumnWidth(2, 3000);
		sheet.setColumnWidth(3, 6000);
		sheet.setColumnWidth(4, 6000);
		sheet.setColumnWidth(5, 6000);
		sheet.setColumnWidth(6, 6000);
		// 创建第一行
		HSSFRow row0 = sheet.createRow(0);
		// 设置行高
		row0.setHeight((short) 1000);
		HSSFCell cell = row0.createCell(0);
		cell.setCellValue(new HSSFRichTextString("商家ID"));
		cell = row0.createCell(1);
		cell.setCellValue(new HSSFRichTextString("店铺名称"));
		cell = row0.createCell(2);
		cell.setCellValue(new HSSFRichTextString("坐席昵称"));
		cell = row0.createCell(3);
		cell.setCellValue(new HSSFRichTextString("用户ID"));
		cell = row0.createCell(4);
		cell.setCellValue(new HSSFRichTextString("进入会话时间"));
		cell = row0.createCell(5);
		cell.setCellValue(new HSSFRichTextString("会话结束时间"));
		cell = row0.createCell(6);
		cell.setCellValue(new HSSFRichTextString("会话内容"));
		List<String> list=new ArrayList<>();
		if (!CollectionUtils.isEmpty(list)) {
			for (int i = 1; i <= list.size(); i++) {
				HSSFRow row = sheet.createRow(i);
				HSSFCell cell1 = row.createCell(0);
				cell1.setCellValue(new HSSFRichTextString(""));
				cell1 = row.createCell(1);
				// 从redis获取店铺名称，获取不到查询数据库
				cell1.setCellValue(new HSSFRichTextString("test"));
				cell1.setCellValue("");
				cell1 = row.createCell(2);
				cell1 = row.createCell(3);
				cell1 = row.createCell(4);
				DateTimeFormatter fa = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss");
				cell1.setCellValue(new Date());
				cell1 = row.createCell(5);

				cell1.setCellValue(new Date());

				cell1 = row.createCell(6);
				cell1.setCellValue("");
			}
		}
		try {
			//  清空response  
			response.reset();
			// 设置生成的文件类型  
			response.setContentType("application/vnd.ms-excel");
			// 设置文件头编码方式和文件名  
			response.setCharacterEncoding("UTF-8");
			response.setHeader("content-Disposition",
					"attachment; filename=" + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
			OutputStream os = response.getOutputStream();
			workbook.write(os);
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
