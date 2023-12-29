package com.mashreq.oa.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
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

import com.mashreq.oa.entity.PaymentData;

public class CompletedRequestExcel {

	private static final Logger logger=LoggerFactory.getLogger(CompletedRequestExcel.class);
	private static XSSFWorkbook wb;
	
	//@Value("${}")
	String file="/app/ownersassociation/documents/reports/CompletedPayments.xlsx";
	
	public  String generateRepotsxlsFile(List<PaymentData> completedRequests) throws IOException {
		logger.info("Generating Reports xl file");
		
		
		Sheet sheet;
		Row row;
		List<String> columnList =new ArrayList<String>();
		columnList.add("Matrix Ref No");
		columnList.add("Sub Product");
		columnList.add("DebitAccountNumberDesc");
		columnList.add("BeneficiaryName");
		columnList.add("Initiator Date");
		columnList.add("Payment Currency");
		columnList.add("Amount");
		columnList.add("Customer Reference");
		columnList.add("InitiatorName/Date & Time");
		columnList.add("Status");
		columnList.add("Remarks");
		columnList.add("Username");
		columnList.add("Service Code");
		columnList.add("Budget Year");
		
		
		
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
			for(PaymentData bean: completedRequests) {
				row =sheet.createRow(rowsize++);
				logger.info("data row:"+row);
				
//				Cell cell0=row.createCell(0);
//				cell0.setCellValue(bean.getMcNameEn()!=null? bean.getMcNameEn():"");
//				cell0.setCellStyle(contentStyle);
//				
				
				Cell cell0 =row.createCell(0);
				cell0.setCellValue(String.valueOf(bean.getMatrixRefNo())!=null? String.valueOf(bean.getMatrixRefNo()):"");
				cell0.setCellStyle(contentStyle);
				
				
				Cell cell1 =row.createCell(1);
				cell1.setCellValue(String.valueOf(bean.getSubProduct())!=null? String.valueOf(bean.getSubProduct()):"");
				cell1.setCellStyle(contentStyle);
				
				
				Cell cell2 =row.createCell(2);
				cell2.setCellValue(String.valueOf(bean.getDebitAccountNumberDesc())!=null? String.valueOf(bean.getDebitAccountNumberDesc()):"");
				cell2.setCellStyle(contentStyle);
				
				
				Cell cell3 =row.createCell(3);
				cell3.setCellValue(String.valueOf(bean.getBeneficiaryName())!=null? String.valueOf(bean.getBeneficiaryName()):"");
				cell3.setCellStyle(contentStyle);
				
				
				Cell cell4 =row.createCell(4);
				cell4.setCellValue(String.valueOf(bean.getDisplayInitiatorDate())!=null? String.valueOf(bean.getDisplayInitiatorDate()):"");
				cell4.setCellStyle(contentStyle);
				
				
				Cell cell5 =row.createCell(5);
				cell5.setCellValue(String.valueOf(bean.getPaymentCurrency())!=null? String.valueOf(bean.getPaymentCurrency()):"");
				cell5.setCellStyle(contentStyle);
				
				Cell cell6 =row.createCell(6);
				cell6.setCellValue(String.valueOf(bean.getInvoiceAmount())!=null? String.valueOf(bean.getInvoiceAmount()):"");
				cell6.setCellStyle(contentStyle);
				
				Cell cell7 =row.createCell(7);
				cell7.setCellValue(String.valueOf(bean.getCustomerReference())!=null? String.valueOf(bean.getCustomerReference()):"");
				cell7.setCellStyle(contentStyle);
				
				Cell cell8 =row.createCell(8);
				cell8.setCellValue(String.valueOf(bean.getInitiatorNameDateTime())!=null? String.valueOf(bean.getInitiatorNameDateTime()):"");
				cell8.setCellStyle(contentStyle);
				
				Cell cell9 =row.createCell(9);
				cell9.setCellValue(String.valueOf(bean.getStatus())!=null? String.valueOf(bean.getStatus()):"");
				cell9.setCellStyle(contentStyle);
				
				Cell cell10 =row.createCell(10);
				cell10.setCellValue(String.valueOf(bean.getRemarks())!=null? String.valueOf(bean.getRemarks()):"");
				cell10.setCellStyle(contentStyle);
				
				Cell cell11 =row.createCell(11);
				cell11.setCellValue(String.valueOf(bean.getUploadedBy())!=null? String.valueOf(bean.getUploadedBy()):"");
				cell11.setCellStyle(contentStyle);
				
				Cell cell12 =row.createCell(12);
				cell12.setCellValue(String.valueOf(bean.getServiceCode())!=null? String.valueOf(bean.getServiceCode()):"");
				cell12.setCellStyle(contentStyle);
				
				Cell cell13 =row.createCell(13);
				cell13.setCellValue(String.valueOf(bean.getBudgetYear())!=null? String.format("%.0f",bean.getBudgetYear()):"");
				cell13.setCellStyle(contentStyle);
				
			}
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(6);
			sheet.autoSizeColumn(7);
			sheet.autoSizeColumn(8);
			sheet.autoSizeColumn(9);
			sheet.autoSizeColumn(10);
			sheet.autoSizeColumn(11);
			sheet.autoSizeColumn(12);
			sheet.autoSizeColumn(13);
			wb.write(new FileOutputStream(file1));
			
		}catch (Exception e) {
			logger.error("exception while exporting data:"+e.getCause());
			
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
