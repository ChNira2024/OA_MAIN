package com.mashreq.oa.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.mashreq.oa.dao.ReportsDao;
import com.mashreq.oa.entity.ReportsOutput;
import com.mashreq.oa.entity.TransactionalReportOutput;

public class TransactionalReportsExcel {

	private static final Logger LOGGER=LoggerFactory.getLogger(TransactionalReportsExcel.class);
	private static XSSFWorkbook wb;
	
	//@Value("${transactionalreport.file.excelpath}")
	String file="/app/ownersassociation/documents/reports/OATransactions.xlsx";
	
	public  String generateRepotsxlsFile(List<TransactionalReportOutput> detailsList) throws IOException {
		LOGGER.info("Generating Reports xl file");
		
		
		Sheet sheet;
		Row row;
		List<String> columnList =new ArrayList<String>();
		columnList.add("Total_Transaction");
		columnList.add("In-Progress");
		columnList.add("Exception");
		columnList.add("Approved");
		columnList.add("Rejected");
		columnList.add("UserName");
		
		
		File file1=File.createTempFile(file, null);
		try {
			int rowsize=0;
			wb=new XSSFWorkbook();
			sheet = wb.createSheet("sheet1");
			
			
			
			XSSFFont headfFont= wb.createFont();
			headfFont.setBold(true);
			headfFont.setColor(IndexedColors.SKY_BLUE.getIndex());
			XSSFFont headerFont =createFont((short)12,true);
			XSSFFont contentFont =createFont((short)10,true);
			
			XSSFCellStyle headerStyle= createStyle(headerFont, HorizontalAlignment.CENTER,true);
			XSSFCellStyle contentStyle= createStyle(contentFont, HorizontalAlignment.LEFT,true);
			
			CellStyle Style=wb.createCellStyle();
			Style.setFont(headfFont);
			
			XSSFFont font = wb.createFont();
			font.setBold(true);
			
			CellStyle Style1=wb.createCellStyle();
			Style1.setFont(font);
			Cell headerCell = null;
			row = sheet.createRow(rowsize++);
			
			for(int i=0;i<columnList.size();i++) {
				headerCell= row.createCell(i);
				headerCell.setCellValue(columnList.get(i));
				headerCell.setCellStyle(headerStyle);
				sheet.autoSizeColumn(i);
			}
			for(TransactionalReportOutput bean: detailsList) {
				row =sheet.createRow(rowsize++);
				LOGGER.info("data row:"+row);
				
				Cell cell0=row.createCell(0);
				cell0.setCellValue(bean.getTotalTransaction()!=null? bean.getTotalTransaction():"");
				cell0.setCellStyle(contentStyle);
				
				
				Cell cell1 =row.createCell(1);
				cell1.setCellValue(bean.getInProgress()!=null? bean.getInProgress():"");
				cell1.setCellStyle(contentStyle);
				
				
				Cell cell2 =row.createCell(2);
				cell2.setCellValue(String.valueOf(bean.getException())!=null? String.valueOf(bean.getException()):"");
				cell2.setCellStyle(contentStyle);
				
				
				Cell cell3 =row.createCell(3);
				cell3.setCellValue(String.valueOf(bean.getApproved())!=null? String.valueOf(bean.getApproved()):"");
				cell3.setCellStyle(contentStyle);
				
				
				Cell cell4 =row.createCell(4);
				cell4.setCellValue(String.valueOf(bean.getRejected())!=null? String.valueOf(bean.getRejected()):"");
				cell4.setCellStyle(contentStyle);
				
				
				Cell cell5 =row.createCell(5);
				cell5.setCellValue(String.valueOf(bean.getUserName())!=null? String.valueOf(bean.getUserName()):"");
				cell5.setCellStyle(contentStyle);
				
				
			}
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(6);
			wb.write(new FileOutputStream(file1));
			
		}catch (Exception e) {
			LOGGER.error("exception while exporting data:"+e.getCause());
			
		}
		
		byte[] base64=Files.readAllBytes(file1.toPath());
		
		String base64Str=Base64.getEncoder().encodeToString(base64);
		
		return base64Str;
		
		
		
	}
	public static XSSFFont createFont(short fontHeight,boolean fontBold) {
		XSSFFont font=wb.createFont();
		font.setBold(fontBold);
		return font;
		
	}

	public static XSSFCellStyle createStyle(XSSFFont font ,HorizontalAlignment cellAlign, boolean cellBorder) {
		org.apache.poi.xssf.usermodel.XSSFCellStyle style=wb.createCellStyle();
		style.setFont(font);
		style.setAlignment(cellAlign);
		
		BorderStyle borderStyle=BorderStyle.THIN;
		if(cellBorder) {
			style.setBorderTop(borderStyle);
			style.setBorderLeft(borderStyle);
			style.setBorderRight(borderStyle);
			style.setBorderBottom(borderStyle);
		}
		return style;
	}
	
	
}
